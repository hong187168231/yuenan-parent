package com.indo.game.service.saba.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.pojo.dto.saba.*;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.vo.callback.saba.*;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.saba.SabaCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;
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
    private TxnsMapper txnsMapper;

    //取得用户的余额
    public Object getBalance(SabaCallBackReq<SabaCallBackParentReq> sabaCallBackReq) {
        SabaCallBackParentReq sabaCallBackParentReq = sabaCallBackReq.getMessage();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackParentReq.getUserId());
        SabaCallBackGetBalanceResp sabaCallBackGetBalanceResp = new SabaCallBackGetBalanceResp();

        if (null == memBaseinfo) {
            sabaCallBackGetBalanceResp.setStatus("203");
            sabaCallBackGetBalanceResp.setMsg("Account Existed");
        } else {
            sabaCallBackGetBalanceResp.setStatus("0");
            sabaCallBackGetBalanceResp.setMsg("Success");
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
            sabaCallBackGetBalanceResp.setBalance(memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
            sabaCallBackGetBalanceResp.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT1));
            sabaCallBackGetBalanceResp.setUserId(sabaCallBackParentReq.getUserId());
        }

        return sabaCallBackGetBalanceResp;
    }


    //投注
    public Object placeBet(SabaCallBackReq<SabaCallBackPlaceBetReq> sabaCallBackReq) {
        SabaCallBackPlaceBetReq sabaCallBackPlaceBetReq = sabaCallBackReq.getMessage();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackPlaceBetReq.getUserId());
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();

        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return sabaCallBackRespError;
        } else {
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            BigDecimal betAmount = null!=sabaCallBackPlaceBetReq.getActualAmount()?sabaCallBackPlaceBetReq.getActualAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
            if (memBaseinfo.getBalance().compareTo(betAmount) == -1) {
                sabaCallBackRespError.setStatus("502");
                sabaCallBackRespError.setMsg("Player Has Insufficient Funds");
                return sabaCallBackRespError;
            } else {
                SabaCallBackPlaceBetResp sabaCallBackPlaceBetResp = new SabaCallBackPlaceBetResp();
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                sabaCallBackPlaceBetResp.setStatus("0");
                sabaCallBackPlaceBetResp.setRefId(sabaCallBackPlaceBetReq.getRefId());
                String rePlatformTxId = GeneratorIdUtil.generateId();
                sabaCallBackPlaceBetResp.setLicenseeTxId(rePlatformTxId);

                Txns txns = new Txns();
                BeanUtils.copyProperties(sabaCallBackPlaceBetReq, txns);
                //游戏商注单号
                txns.setPlatformTxId(sabaCallBackPlaceBetReq.getRefId());
                txns.setRePlatformTxId(rePlatformTxId);
//                //此交易是否是投注 true是投注 false 否
////        txns.setbet();
//                //玩家 ID
//                txns.setUserId(apiRequestData.getUid());
//                //玩家货币代码
//                txns.setCurrency(apiRequestData.getCurrency());
                //平台代码
                txns.setPlatform(gameParentPlatform.getPlatformCode());
                //平台英文名称
                txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
                //平台中文名称
                txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
                //平台游戏类型
                txns.setGameType(gameCategory.getGameType());
                //游戏分类ID
                txns.setCategoryId(gameCategory.getId());
                //游戏分类名称
                txns.setCategoryName(gameCategory.getGameName());
                //平台游戏代码
                txns.setGameCode(gamePlatform.getPlatformCode());
                //游戏名称
                txns.setGameName(gamePlatform.getPlatformEnName());
                //下注金额
                txns.setBetAmount(betAmount);
                txns.setWinningAmount(betAmount.negate());
                txns.setWinAmount(betAmount);
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txns.setMethod("PlaceBet");
                txnsMapper.insert(txns);
                return sabaCallBackPlaceBetResp;
            }
        }
    }

    //确认投注查询
    public Object confirmBet(SabaCallBackReq<SabaCallBackConfirmBetReq<TicketInfoReq>> sabaCallBackReq) {
        SabaCallBackConfirmBetReq<TicketInfoReq> sabaCallBackConfirmBetReq = sabaCallBackReq.getMessage();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackConfirmBetReq.getUserId());
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return sabaCallBackRespError;
        } else {
            List<TicketInfoReq> list = sabaCallBackConfirmBetReq.getTxns();
            BigDecimal balance = memBaseinfo.getBalance();
            for (TicketInfoReq t : list) {
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Txns::getPlatformTxId, sabaCallBackConfirmBetReq.getOperationId());
                wrapper.eq(Txns::getUserId, sabaCallBackConfirmBetReq.getUserId());
                wrapper.eq(Txns::getRePlatformTxId, t.getRefId());
                wrapper.eq(Txns::getStatus, "PlaceBet");
                Txns txns = txnsMapper.selectOne(wrapper);
                if (txns.getRealBetAmount().compareTo(t.getActualAmount()) != 0) {
                    txns.setOdds(t.getOdds());
                    txns.setRealBetAmount(t.getActualAmount());
                    txnsMapper.updateById(txns);
                    BigDecimal dAmount = txns.getRealBetAmount().subtract(t.getActualAmount());
                    if (txns.getRealBetAmount().compareTo(t.getActualAmount()) == -1) {
                        gameCommonService.updateUserBalance(memBaseinfo, dAmount, GoldchangeEnum.ADJUST_BET, TradingEnum.SPENDING);
                    } else {
                        gameCommonService.updateUserBalance(memBaseinfo, dAmount, GoldchangeEnum.ADJUST_BET, TradingEnum.INCOME);
                    }
                    balance = balance.subtract(dAmount);
                }
            }
            SabaCallBackConfirmBetResp sabaCallBackConfirmBetResp = new SabaCallBackConfirmBetResp();
            sabaCallBackConfirmBetResp.setStatus("0");
            sabaCallBackConfirmBetResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            return sabaCallBackConfirmBetResp;
        }
    }

    //取消投注
    public Object cancelBet(SabaCallBackReq<SabaCallBackCancelBetReq<TradingInfoReq>> sabaCallBackReq) {

        SabaCallBackCancelBetReq<TradingInfoReq> sabaCallBackCancelBetReq = sabaCallBackReq.getMessage();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackCancelBetReq.getUserId());
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return sabaCallBackRespError;
        } else {
            List<TradingInfoReq> list = sabaCallBackCancelBetReq.getTxns();
            BigDecimal balance = memBaseinfo.getBalance();
            for (TradingInfoReq t : list) {
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Txns::getPlatformTxId, sabaCallBackCancelBetReq.getOperationId());
                wrapper.eq(Txns::getUserId, sabaCallBackCancelBetReq.getUserId());
                wrapper.eq(Txns::getRePlatformTxId, t.getRefId());
                wrapper.eq(Txns::getStatus, "PlaceBet");
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                oldTxns.setMethod("CancelBet");
                txnsMapper.updateById(oldTxns);
                gameCommonService.updateUserBalance(memBaseinfo, oldTxns.getRealBetAmount(), GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                balance = balance.add(oldTxns.getRealBetAmount());
                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setRealBetAmount(t.getCreditAmount());//需增加在玩家的金额。
                txns.setRealWinAmount(t.getDebitAmount());//需从玩家扣除的金额
            }
            SabaCallBackConfirmBetResp sabaCallBackConfirmBetResp = new SabaCallBackConfirmBetResp();
            sabaCallBackConfirmBetResp.setStatus("0");
            sabaCallBackConfirmBetResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            return sabaCallBackConfirmBetResp;
        }

    }

    //结算投注
    public Object settle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq) {
        SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> sabaCallBackSettleReq = sabaCallBackReq.getMessage();

        List<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> settleTradingInfoReqList = sabaCallBackSettleReq.getTxns();
        for (SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>> settleTradingInfoReq : settleTradingInfoReqList) {
            List<ParlayDetailReq<SystemParlayDetailReq>> parlayDetailReqList = settleTradingInfoReq.getParlayDetail();
            for (ParlayDetailReq<SystemParlayDetailReq> parlayDetailReq : parlayDetailReqList) {
                List<SystemParlayDetailReq> systemParlayDetailReqList = parlayDetailReq.getSystemParlayDetail();
                for (SystemParlayDetailReq systemParlayDetailReq : systemParlayDetailReqList) {

                }
            }
        }

        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return sabaCallBackParentResp;
    }

    //重新结算投注
    public Object resettle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq) {
        SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> sabaCallBackSettleReq = sabaCallBackReq.getMessage();

        List<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> settleTradingInfoReqList = sabaCallBackSettleReq.getTxns();
        for (SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>> settleTradingInfoReq : settleTradingInfoReqList) {
            List<ParlayDetailReq<SystemParlayDetailReq>> parlayDetailReqList = settleTradingInfoReq.getParlayDetail();
            for (ParlayDetailReq<SystemParlayDetailReq> parlayDetailReq : parlayDetailReqList) {
                List<SystemParlayDetailReq> systemParlayDetailReqList = parlayDetailReq.getSystemParlayDetail();
                for (SystemParlayDetailReq systemParlayDetailReq : systemParlayDetailReqList) {

                }
            }
        }

        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return sabaCallBackParentResp;
    }

    //撤销结算投注
    public Object unsettle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq) {
        SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> sabaCallBackSettleReq = sabaCallBackReq.getMessage();

        List<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>> settleTradingInfoReqList = sabaCallBackSettleReq.getTxns();
        for (SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>> settleTradingInfoReq : settleTradingInfoReqList) {
            List<ParlayDetailReq<SystemParlayDetailReq>> parlayDetailReqList = settleTradingInfoReq.getParlayDetail();
            for (ParlayDetailReq<SystemParlayDetailReq> parlayDetailReq : parlayDetailReqList) {
                List<SystemParlayDetailReq> systemParlayDetailReqList = parlayDetailReq.getSystemParlayDetail();
                for (SystemParlayDetailReq systemParlayDetailReq : systemParlayDetailReqList) {

                }
            }
        }

        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return sabaCallBackParentResp;
    }

    //投注-仅支援欧洲盘
    public Object placeBetParlay(SabaCallBackReq<SabaCallBackPlaceBetParlayReq> sabaCallBackReq) {

        SabaCallBackPlaceBetParlayReq sabaCallBackPlaceBetParlayReq = sabaCallBackReq.getMessage();
        List<ComboInfo> comboInfoList = sabaCallBackPlaceBetParlayReq.getTxns();
        List<TicketInfoMapping> ticketInfoMappingList = new ArrayList<>();
        for (ComboInfo comboInfo : comboInfoList) {
            TicketInfoMapping ticketInfoMapping = new TicketInfoMapping();
            ticketInfoMapping.setRefId(comboInfo.getRefId());
            ticketInfoMapping.setLicenseeTxId(GeneratorIdUtil.generateId());
            ticketInfoMappingList.add(ticketInfoMapping);
        }
        SabaCallBackPlaceBetParlayResp sabaCallBackPlaceBetParlayResp = new SabaCallBackPlaceBetParlayResp();
        sabaCallBackPlaceBetParlayResp.setStatus("0");
        sabaCallBackPlaceBetParlayResp.setTxns(ticketInfoMappingList);
        return sabaCallBackPlaceBetParlayResp;
    }

    //当沙巴体育通过 PlaceBetParlay 方法收到成功结果，沙巴体育将会呼叫 ConfirmBetParlay
    public Object confirmBetParlay(SabaCallBackReq<SabaCallBackConfirmBetParlayReq> sabaCallBackReq) {

        SabaCallBackConfirmBetParlayReq sabaCallBackConfirmBetParlayReq = sabaCallBackReq.getMessage();
        List<ConfirmBetParlayTicketInfoReq> txns = sabaCallBackConfirmBetParlayReq.getTxns();
        for (ConfirmBetParlayTicketInfoReq confirmBetParlayTicketInfoReq : txns) {

        }
        SabaCallBackConfirmBetResp sabaCallBackConfirmBetResp = new SabaCallBackConfirmBetResp();
        sabaCallBackConfirmBetResp.setStatus("0");
        return sabaCallBackConfirmBetResp;
    }

    //厂商提供此方法，沙巴体育通过呼叫此方法提供下注细节给厂商。本方法支持快乐彩、彩票、桌面游戏产品
    public Object placeBet3rd(SabaCallBackReq<SabaCallBackPlaceBet3rdReq<PlaceBet3rdTicketInfoReq>> sabaCallBackReq) {

        SabaCallBackPlaceBet3rdReq<PlaceBet3rdTicketInfoReq> sabaCallBackPlaceBet3rdReq = sabaCallBackReq.getMessage();

        List<PlaceBet3rdTicketInfoReq> placeBet3rdTicketInfoReqList = sabaCallBackPlaceBet3rdReq.getTicketList();
        for (PlaceBet3rdTicketInfoReq placeBet3rdTicketInfoReq : placeBet3rdTicketInfoReqList) {

        }

        SabaCallBackPlaceBet3rdResp<TicketInfoMapping> sabaCallBackPlaceBet3rdResp = new SabaCallBackPlaceBet3rdResp<>();
        sabaCallBackPlaceBet3rdResp.setStatus("0");
        return sabaCallBackPlaceBet3rdResp;
    }

    public Object confirmBet3rd(SabaCallBackReq<SabaCallBackConfirmBet3rdReq<ConfirmBet3rdTicketInfoReq>> sabaCallBackReq) {

        SabaCallBackConfirmBet3rdReq<ConfirmBet3rdTicketInfoReq> sabaCallBackConfirmBet3rdReq = sabaCallBackReq.getMessage();

        SabaCallBackConfirmBet3rdResp sabaCallBackConfirmBet3rdResp = new SabaCallBackConfirmBet3rdResp();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackConfirmBet3rdReq.getUserId());

        if (null == memBaseinfo) {
            sabaCallBackConfirmBet3rdResp.setStatus("203");
            sabaCallBackConfirmBet3rdResp.setMsg("Account Existed");
        } else {
            sabaCallBackConfirmBet3rdResp.setStatus("0");
            sabaCallBackConfirmBet3rdResp.setBalance(memBaseinfo.getBalance());
        }
        return sabaCallBackConfirmBet3rdResp;
    }

    //当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易
    public Object cashOut(SabaCallBackReq<SabaCallBackCashOutReq<CashOutTicketInfoReq>> sabaCallBackReq) {

        SabaCallBackCashOutReq<CashOutTicketInfoReq> sabaCallBackCashOutReq = sabaCallBackReq.getMessage();
        List<CashOutTicketInfoReq> cashOutTicketInfoReqList = sabaCallBackCashOutReq.getTxns();
        for (CashOutTicketInfoReq cashOutTicketInfoReq : cashOutTicketInfoReqList) {

        }
        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return sabaCallBackParentResp;
    }

    //因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。
    public Object updateBet(SabaCallBackReq<SabaCallBackCashOutReq<UpdateBetTicketInfoReq>> sabaCallBackReq) {

        SabaCallBackCashOutReq<UpdateBetTicketInfoReq> sabaCallBackCashOutReq = sabaCallBackReq.getMessage();
        List<UpdateBetTicketInfoReq> updateBetTicketInfoReqList = sabaCallBackCashOutReq.getTxns();
        for (UpdateBetTicketInfoReq updateBetTicketInfoReq : updateBetTicketInfoReqList) {

        }


        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return sabaCallBackParentResp;
    }

    //检查沙巴体育与厂商之间的连结是否有效。
    public Object healthcheck(SabaCallBackReq<HealthCheckReq> sabaCallBackReq) {
        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return sabaCallBackParentResp;
    }
}
