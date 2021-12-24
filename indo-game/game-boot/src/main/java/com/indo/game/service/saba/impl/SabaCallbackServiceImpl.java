package com.indo.game.service.saba.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.SnowflakeIdWorker;
import com.indo.game.mapper.saba.SabaOperInfoMapper;
import com.indo.game.pojo.entity.saba.*;
import com.indo.game.pojo.vo.callback.saba.*;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.saba.SabaCallbackService;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SabaCallbackServiceImpl implements SabaCallbackService {

    @Autowired
    private GameCommonService gameCommonService;

    @Autowired
    private SabaOperInfoMapper sabaOperInfoMapper;

    //取得用户的余额
    public String getBalance(SabaCallBackReq<SabaCallBackParentReq> sabaCallBackReq) {
        SabaCallBackParentReq sabaCallBackParentReq = sabaCallBackReq.getMessage();
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sabaCallBackParentReq.getUserId());
        SabaCallBackGetBalanceResp sabaCallBackGetBalanceResp = new SabaCallBackGetBalanceResp();

        if (null == memBaseinfo) {
            sabaCallBackGetBalanceResp.setStatus("203");
            sabaCallBackGetBalanceResp.setMsg("Account Existed");
        }else {
            sabaCallBackGetBalanceResp.setStatus("0");
            sabaCallBackGetBalanceResp.setMsg("Success");
            sabaCallBackGetBalanceResp.setBalance(memBaseinfo.getBalance());
            sabaCallBackGetBalanceResp.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT1));
            sabaCallBackGetBalanceResp.setUserId(sabaCallBackParentReq.getUserId());
        }

        return JSONObject.toJSONString(sabaCallBackGetBalanceResp);
    }


    //投注
    public String placeBet(SabaCallBackReq<SabaCallBackPlaceBetReq> sabaCallBackReq) {
        SabaCallBackPlaceBetReq sabaCallBackPlaceBetReq = sabaCallBackReq.getMessage();
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sabaCallBackPlaceBetReq.getUserId());
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();

        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return JSONObject.toJSONString(sabaCallBackRespError);
        }else {
            BigDecimal betAmount = sabaCallBackPlaceBetReq.getActualAmount();
            if(memBaseinfo.getBalance().compareTo(betAmount) == -1){
                sabaCallBackRespError.setStatus("502");
                sabaCallBackRespError.setMsg("Player Has Insufficient Funds");
                return JSONObject.toJSONString(sabaCallBackRespError);
            }else {
                SabaCallBackPlaceBetResp sabaCallBackPlaceBetResp = new SabaCallBackPlaceBetResp();
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                sabaCallBackPlaceBetResp.setStatus("0");
                sabaCallBackPlaceBetResp.setRefId(sabaCallBackPlaceBetReq.getRefId());
                sabaCallBackPlaceBetResp.setLicenseeTxId(SnowflakeIdWorker.createOrderSn());

                SabaOperInfo sabaOperInfo = new SabaOperInfo();
                BeanUtils.copyProperties(sabaCallBackPlaceBetReq,sabaOperInfo);
                sabaOperInfo.setCreateTime(new Date());
                sabaOperInfo.setOperStatus("PlaceBet");
                sabaOperInfoMapper.insert(sabaOperInfo);
                return JSONObject.toJSONString(sabaCallBackPlaceBetResp);
            }
        }
    }
    //确认投注查询
    public String confirmBet(SabaCallBackReq<SabaCallBackConfirmBetReq<TicketInfoReq>> sabaCallBackReq){
        SabaCallBackConfirmBetReq<TicketInfoReq> sabaCallBackConfirmBetReq = sabaCallBackReq.getMessage();
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sabaCallBackConfirmBetReq.getUserId());
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();

        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return JSONObject.toJSONString(sabaCallBackRespError);
        }else {
            List<TicketInfoReq> list = sabaCallBackConfirmBetReq.getTxns();
            BigDecimal balance = memBaseinfo.getBalance();
            for (TicketInfoReq t : list){
                LambdaQueryWrapper<SabaOperInfo> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SabaOperInfo::getOperationId,sabaCallBackConfirmBetReq.getOperationId());
                wrapper.eq(SabaOperInfo::getUserId,sabaCallBackConfirmBetReq.getUserId());
                wrapper.eq(SabaOperInfo::getRefId,t.getRefId());
                wrapper.eq(SabaOperInfo::getOperStatus,"PlaceBet");
                SabaOperInfo sabaOperInfo = sabaOperInfoMapper.selectOne(wrapper);
                if(sabaOperInfo.getActualAmount().compareTo(t.getActualAmount()) != 0){
                    sabaOperInfo.setOdds(t.getOdds());
                    sabaOperInfo.setActualAmount(t.getActualAmount());
                    sabaOperInfoMapper.updateById(sabaOperInfo);
                    BigDecimal dAmount = sabaOperInfo.getActualAmount().subtract(t.getActualAmount());
                    if(sabaOperInfo.getActualAmount().compareTo(t.getActualAmount()) == -1){
                        gameCommonService.updateUserBalance(memBaseinfo, dAmount, GoldchangeEnum.ADJUST_BET, TradingEnum.SPENDING);
                    }else {
                        gameCommonService.updateUserBalance(memBaseinfo, dAmount, GoldchangeEnum.ADJUST_BET, TradingEnum.INCOME);
                    }
                    balance = balance.subtract(dAmount);
                }
            }
            SabaCallBackConfirmBetResp sabaCallBackConfirmBetResp = new SabaCallBackConfirmBetResp();
            sabaCallBackConfirmBetResp.setStatus("0");
            sabaCallBackConfirmBetResp.setBalance(balance);
            return JSONObject.toJSONString(sabaCallBackConfirmBetResp);
        }
    }

    //取消投注
    public String cancelBet(SabaCallBackReq<SabaCallBackCancelBetReq<TradingInfoReq>> sabaCallBackReq) {

        SabaCallBackCancelBetReq<TradingInfoReq> sabaCallBackCancelBetReq = sabaCallBackReq.getMessage();
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sabaCallBackCancelBetReq.getUserId());
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();

        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return JSONObject.toJSONString(sabaCallBackRespError);
        }else {
            List<TradingInfoReq> list = sabaCallBackCancelBetReq.getTxns();
            BigDecimal balance = memBaseinfo.getBalance();
            for (TradingInfoReq t : list){
                LambdaQueryWrapper<SabaOperInfo> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SabaOperInfo::getOperationId,sabaCallBackCancelBetReq.getOperationId());
                wrapper.eq(SabaOperInfo::getUserId,sabaCallBackCancelBetReq.getUserId());
                wrapper.eq(SabaOperInfo::getRefId,t.getRefId());
                wrapper.eq(SabaOperInfo::getOperStatus,"PlaceBet");
                SabaOperInfo oldSabaOperInfo = sabaOperInfoMapper.selectOne(wrapper);
                oldSabaOperInfo.setOperStatus("CancelBet");
                sabaOperInfoMapper.updateById(oldSabaOperInfo);
                gameCommonService.updateUserBalance(memBaseinfo, oldSabaOperInfo.getActualAmount(), GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                balance = balance.add(oldSabaOperInfo.getActualAmount());
                SabaOperInfo sabaOperInfo = new SabaOperInfo();
                BeanUtils.copyProperties(oldSabaOperInfo,sabaOperInfo);
                sabaOperInfo.setCreditAmount(t.getCreditAmount());
                sabaOperInfo.setDebitAmount(t.getDebitAmount());
            }
            SabaCallBackConfirmBetResp sabaCallBackConfirmBetResp = new SabaCallBackConfirmBetResp();
            sabaCallBackConfirmBetResp.setStatus("0");
            sabaCallBackConfirmBetResp.setBalance(balance);
            return JSONObject.toJSONString(sabaCallBackConfirmBetResp);
        }

    }

    //结算投注
    public String settle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq) {
        SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> sabaCallBackSettleReq = sabaCallBackReq.getMessage();

        List<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> settleTradingInfoReqList = sabaCallBackSettleReq.getTxns();
        for (SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>> settleTradingInfoReq:settleTradingInfoReqList){
            List<ParlayDetailReq<SystemParlayDetailReq>> parlayDetailReqList = settleTradingInfoReq.getParlayDetail();
            for (ParlayDetailReq<SystemParlayDetailReq> parlayDetailReq :parlayDetailReqList){
                List<SystemParlayDetailReq> systemParlayDetailReqList = parlayDetailReq.getSystemParlayDetail();
                for (SystemParlayDetailReq systemParlayDetailReq: systemParlayDetailReqList){

                }
            }
        }

        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return JSONObject.toJSONString(sabaCallBackParentResp);
    }

    //重新结算投注
    public String resettle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq) {
        SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> sabaCallBackSettleReq = sabaCallBackReq.getMessage();

        List<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> settleTradingInfoReqList = sabaCallBackSettleReq.getTxns();
        for (SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>> settleTradingInfoReq:settleTradingInfoReqList){
            List<ParlayDetailReq<SystemParlayDetailReq>> parlayDetailReqList = settleTradingInfoReq.getParlayDetail();
            for (ParlayDetailReq<SystemParlayDetailReq> parlayDetailReq :parlayDetailReqList){
                List<SystemParlayDetailReq> systemParlayDetailReqList = parlayDetailReq.getSystemParlayDetail();
                for (SystemParlayDetailReq systemParlayDetailReq: systemParlayDetailReqList){

                }
            }
        }

        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return JSONObject.toJSONString(sabaCallBackParentResp);
    }

    //撤销结算投注
    public String unsettle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq) {
        SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> sabaCallBackSettleReq = sabaCallBackReq.getMessage();

        List<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> settleTradingInfoReqList = sabaCallBackSettleReq.getTxns();
        for (SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>> settleTradingInfoReq:settleTradingInfoReqList){
            List<ParlayDetailReq<SystemParlayDetailReq>> parlayDetailReqList = settleTradingInfoReq.getParlayDetail();
            for (ParlayDetailReq<SystemParlayDetailReq> parlayDetailReq :parlayDetailReqList){
                List<SystemParlayDetailReq> systemParlayDetailReqList = parlayDetailReq.getSystemParlayDetail();
                for (SystemParlayDetailReq systemParlayDetailReq: systemParlayDetailReqList){

                }
            }
        }

        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return JSONObject.toJSONString(sabaCallBackParentResp);
    }

    //投注-仅支援欧洲盘
    public String placeBetParlay(SabaCallBackReq<SabaCallBackPlaceBetParlayReq> sabaCallBackReq){

        SabaCallBackPlaceBetParlayReq sabaCallBackPlaceBetParlayReq = sabaCallBackReq.getMessage();
        List<ComboInfo> comboInfoList = sabaCallBackPlaceBetParlayReq.getTxns();
        List<TicketInfoMapping> ticketInfoMappingList = new ArrayList<>();
        for (ComboInfo comboInfo : comboInfoList){
            TicketInfoMapping ticketInfoMapping = new TicketInfoMapping();
            ticketInfoMapping.setRefId(comboInfo.getRefId());
            ticketInfoMapping.setLicenseeTxId(SnowflakeIdWorker.createOrderSn());
            ticketInfoMappingList.add(ticketInfoMapping);
        }
        SabaCallBackPlaceBetParlayResp sabaCallBackPlaceBetParlayResp = new SabaCallBackPlaceBetParlayResp();
        sabaCallBackPlaceBetParlayResp.setStatus("0");
        sabaCallBackPlaceBetParlayResp.setTxns(ticketInfoMappingList);
        return JSONObject.toJSONString(sabaCallBackPlaceBetParlayResp);
    }

    //当沙巴体育通过 PlaceBetParlay 方法收到成功结果，沙巴体育将会呼叫 ConfirmBetParlay
    public String confirmBetParlay(SabaCallBackReq<SabaCallBackConfirmBetParlayReq> sabaCallBackReq){

        SabaCallBackConfirmBetParlayReq sabaCallBackConfirmBetParlayReq = sabaCallBackReq.getMessage();
        List<ConfirmBetParlayTicketInfoReq> txns = sabaCallBackConfirmBetParlayReq.getTxns();
        for(ConfirmBetParlayTicketInfoReq confirmBetParlayTicketInfoReq : txns){

        }
        SabaCallBackConfirmBetResp sabaCallBackConfirmBetResp = new SabaCallBackConfirmBetResp();
        sabaCallBackConfirmBetResp.setStatus("0");
        return JSONObject.toJSONString(sabaCallBackConfirmBetResp);
    }

    //厂商提供此方法，沙巴体育通过呼叫此方法提供下注细节给厂商。本方法支持快乐彩、彩票、桌面游戏产品
    public String placeBet3rd(SabaCallBackReq<SabaCallBackPlaceBet3rdReq<PlaceBet3rdTicketInfoReq>> sabaCallBackReq){

        SabaCallBackPlaceBet3rdReq<PlaceBet3rdTicketInfoReq> sabaCallBackPlaceBet3rdReq = sabaCallBackReq.getMessage();

        List<PlaceBet3rdTicketInfoReq> placeBet3rdTicketInfoReqList  = sabaCallBackPlaceBet3rdReq.getTicketList();
        for(PlaceBet3rdTicketInfoReq placeBet3rdTicketInfoReq : placeBet3rdTicketInfoReqList){

        }

        SabaCallBackPlaceBet3rdResp<TicketInfoMapping> sabaCallBackPlaceBet3rdResp = new SabaCallBackPlaceBet3rdResp<>();
        sabaCallBackPlaceBet3rdResp.setStatus("0");
        return JSONObject.toJSONString(sabaCallBackPlaceBet3rdResp);
    }
    public String confirmBet3rd(SabaCallBackReq<SabaCallBackConfirmBet3rdReq<ConfirmBet3rdTicketInfoReq>> sabaCallBackReq){

        SabaCallBackConfirmBet3rdReq<ConfirmBet3rdTicketInfoReq> sabaCallBackConfirmBet3rdReq = sabaCallBackReq.getMessage();

        SabaCallBackConfirmBet3rdResp sabaCallBackConfirmBet3rdResp = new SabaCallBackConfirmBet3rdResp();
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sabaCallBackConfirmBet3rdReq.getUserId());

        if (null == memBaseinfo) {
            sabaCallBackConfirmBet3rdResp.setStatus("203");
            sabaCallBackConfirmBet3rdResp.setMsg("Account Existed");
        }else {
            sabaCallBackConfirmBet3rdResp.setStatus("0");
            sabaCallBackConfirmBet3rdResp.setBalance(memBaseinfo.getBalance());
        }
        return JSONObject.toJSONString(sabaCallBackConfirmBet3rdResp);
    }
    //当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易
    public String cashOut(SabaCallBackReq<SabaCallBackCashOutReq<CashOutTicketInfoReq>> sabaCallBackReq){

        SabaCallBackCashOutReq<CashOutTicketInfoReq> sabaCallBackCashOutReq = sabaCallBackReq.getMessage();
        List<CashOutTicketInfoReq> cashOutTicketInfoReqList = sabaCallBackCashOutReq.getTxns();
        for(CashOutTicketInfoReq cashOutTicketInfoReq:cashOutTicketInfoReqList){

        }
        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return JSONObject.toJSONString(sabaCallBackParentResp);
    }

    //因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。
    public String updateBet(SabaCallBackReq<SabaCallBackCashOutReq<UpdateBetTicketInfoReq>> sabaCallBackReq){

        SabaCallBackCashOutReq<UpdateBetTicketInfoReq> sabaCallBackCashOutReq = sabaCallBackReq.getMessage();
        List<UpdateBetTicketInfoReq> updateBetTicketInfoReqList = sabaCallBackCashOutReq.getTxns();
        for(UpdateBetTicketInfoReq updateBetTicketInfoReq:updateBetTicketInfoReqList){

        }


        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return JSONObject.toJSONString(sabaCallBackParentResp);
    }

    //检查沙巴体育与厂商之间的连结是否有效。
    public String healthcheck(SabaCallBackReq<HealthCheckReq> sabaCallBackReq){
        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return JSONObject.toJSONString(sabaCallBackParentResp);
    }
}