package com.indo.game.service.ug.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.ug.*;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.vo.callback.ug.*;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.ug.UgCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UgCallbackServiceImpl implements UgCallbackService {

    @Autowired
    private GameCommonService gameCommonService;

    @Autowired
    private TxnsMapper txnsMapper;



    //玩家登入验证URL
    public Object checkLogin(UgCallBackCheckLoginReq ugCallBackCheckLoginReq) {
        UgCallBackCheckLoginResp ugCallBackCheckLoginResp = new UgCallBackCheckLoginResp();
        if(!this.chechkApiPassword(ugCallBackCheckLoginReq.getApiPassword())){
            ugCallBackCheckLoginResp.setCode("000010");
            ugCallBackCheckLoginResp.setData(false);
            ugCallBackCheckLoginResp.setMsg("API PASSWORD ERROR");
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ugCallBackCheckLoginReq.getUserId());
        if (null == memBaseinfo) {
            ugCallBackCheckLoginResp.setCode("100001");
            ugCallBackCheckLoginResp.setData(false);
            ugCallBackCheckLoginResp.setMsg("MEMBER ACCOUNT IS NOT EXIST");
        } else {
            ugCallBackCheckLoginResp.setCode("000000");
            ugCallBackCheckLoginResp.setData(true);
            ugCallBackCheckLoginResp.setMsg("SUCCESS");
        }

        return ugCallBackCheckLoginResp;
    }

    //取得用户的余额
    public Object getBalance(UgCallBackGetBalanceReq ugCallBackGetBalanceReq) {

        UgCallBackGetBalanceResp ugCallBackGetBalanceResp = new UgCallBackGetBalanceResp();

        if(!this.chechkApiPassword(ugCallBackGetBalanceReq.getApiPassword())){
            ugCallBackGetBalanceResp.setCode("000010");
            ugCallBackGetBalanceResp.setMsg("API PASSWORD ERROR");
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ugCallBackGetBalanceReq.getUserId());
        if (null == memBaseinfo) {
            ugCallBackGetBalanceResp.setCode("100001");
            ugCallBackGetBalanceResp.setMsg("MEMBER ACCOUNT IS NOT EXIST");
        } else {
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.UG_PLATFORM_CODE);
            ugCallBackGetBalanceResp.setCode("000000");
            ugCallBackGetBalanceResp.setData(memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
            ugCallBackGetBalanceResp.setMsg("SUCCESS");
        }

        return ugCallBackGetBalanceResp;
    }


    //加余额/扣除余额
    public Object transfer(UgCallBackTransferReq<UgCallBackTransactionItemReq> ugCallBackTransactionItemReqUgCallBackTransferReq) {
        UgCallBackBalanceResp ugCallBackBalanceResp = new UgCallBackBalanceResp();
        if(!this.chechkApiPassword(ugCallBackTransactionItemReqUgCallBackTransferReq.getApiPassword())){
            ugCallBackBalanceResp.setCode("000010");
            ugCallBackBalanceResp.setMsg("API PASSWORD ERROR");
        }

        List<UgCallBackTransactionItemReq> ugCallBackTransactionItemReqList = ugCallBackTransactionItemReqUgCallBackTransferReq.getData();

        List<UgCallBackSubBalanceResp> ugCallBackBalanceRespList = new ArrayList<>();
        int errorCode = 0;
        String errorMessage = "";
        BigDecimal balance = BigDecimal.valueOf(0);
        MemTradingBO memBaseinfo = new MemTradingBO();
        GameCategory gameCategory = new GameCategory();
        GameParentPlatform gameParentPlatform = new GameParentPlatform();
        List<GamePlatform> gamePlatformList;
        GamePlatform gamePlatform = new GamePlatform();

        for (int i=0;i<ugCallBackTransactionItemReqList.size();i++){
//        for (UgCallBackTransactionItemReq ugCallBackTransactionItemReq : ugCallBackTransactionItemReqList) {
            UgCallBackTransactionItemReq ugCallBackTransactionItemReq = JSONObject.parseObject(JSONObject.toJSONString(ugCallBackTransactionItemReqList.get(i)),UgCallBackTransactionItemReq.class);;
            UgCallBackSubBalanceResp ugCallBackSubBalanceResp = new UgCallBackSubBalanceResp();
            if(i==0) {
                gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.UG_PLATFORM_CODE);
                gamePlatformList = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.UG_PLATFORM_CODE);
                gamePlatform = gamePlatformList.get(0);
                gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            }
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Settle"));
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getPlatformTxId, ugCallBackTransactionItemReq.getTicketId());
            wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
            wrapper.eq(Txns::getRoundId,ugCallBackTransactionItemReq.getTxnId());
            wrapper.eq(Txns::getUserId, ugCallBackTransactionItemReq.getUserId());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            memBaseinfo = gameCommonService.getMemTradingInfo(ugCallBackTransactionItemReq.getUserId());
            balance = memBaseinfo.getBalance();
            if (null == memBaseinfo) {
                ugCallBackBalanceResp.setCode("100001");
                ugCallBackBalanceResp.setMsg("MEMBER ACCOUNT IS NOT EXIST");
                continue;
            } else {

                BigDecimal betAmount = null!=ugCallBackTransactionItemReq.getAmount()?ugCallBackTransactionItemReq.getAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                String method = "Place Bet";
                if (ugCallBackTransactionItemReq.isBet()) {//此交易是否是投注
                    if (balance.compareTo(betAmount.abs()) == -1) {
                        ugCallBackSubBalanceResp.setCode("300004");
                        ugCallBackSubBalanceResp.setMsg("INSUFFICIENT BALANCE");
                        continue;
                    }
                    balance = balance.subtract(betAmount.abs());
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                } else {
                    method = "Settle";
                    if (BigDecimal.ZERO.compareTo(betAmount.abs()) == -1) {
                        balance = balance.subtract(betAmount.abs());
                        gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.BETNSETTLE, TradingEnum.SPENDING);
                    } else {
                        balance = balance.add(betAmount.abs());
                        gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.BETNSETTLE, TradingEnum.INCOME);
                    }
                }


                ugCallBackSubBalanceResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
//创建时间
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                Txns txns = new Txns();
                if(null!=oldTxns) {
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setId(null);
                    oldTxns.setStatus(method);
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                }else {
                    //游戏商注单号
                    txns.setPlatformTxId(ugCallBackTransactionItemReq.getTicketId());
                    //此交易是否是投注 true是投注 false 否
//        txns.setbet();
                    //玩家 ID
                    txns.setUserId(ugCallBackTransactionItemReq.getUserId());
                    //玩家货币代码
                    txns.setCurrency(gameParentPlatform.getCurrencyType());
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
                    //游戏平台的下注项目
//        txns.setbetType();

                    //玩家下注时间
                    txns.setBetTime(DateUtils.getNewFormatDateString(new Date()));
                    //游戏商的回合识别码
                    txns.setRoundId(ugCallBackTransactionItemReq.getTxnId());
                    //下注金额
                    txns.setBetAmount(betAmount);

                }
                txns.setWinningAmount(betAmount);
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(betAmount);
                //真实返还金额,游戏赢分
                txns.setRealWinAmount(betAmount);
                //返还金额 (包含下注金额)
                txns.setWinAmount(betAmount);
                //游戏讯息会由游戏商以 JSON 格式提供
//        txns.setgameInfo();
                //更新时间 (遵循 ISO8601 格式)
                txns.setUpdateTime(DateUtils.getNewFormatDateString(new Date()));

                //赔率
//        txns.setodds();
                //赔率类型
//        txns.setoddsType();
                //打赏资讯，此参数仅游戏商有提供资讯时才会出现
//        txns.settipinfo();
                //操作状态
                txns.setStatus("Running");
                txns.setMethod(method);

                //余额
                txns.setBalance(balance);

                txns.setCreateTime(dateStr);

                txnsMapper.insert(txns);
                ugCallBackSubBalanceResp.setCode("000000");
                ugCallBackSubBalanceResp.setMsg("SUCCESS");
            }
            ugCallBackBalanceRespList.add(ugCallBackSubBalanceResp);
        }
        ugCallBackBalanceResp.setCode("000000");
        ugCallBackBalanceResp.setMsg("SUCCESS");
        ugCallBackBalanceResp.setData(ugCallBackBalanceRespList);

        return ugCallBackBalanceResp;

    }


    // 取消交易
    public Object cancel(UgCallBackCancelReq<UgCallBackCancelItemReq> ugCallBackCancelReq) {

        UgCallBackCancelResp ugCallBackCancelResp = new UgCallBackCancelResp();
        ugCallBackCancelResp.setCode("000000");
        ugCallBackCancelResp.setMsg("SUCCESS");
        if(!this.chechkApiPassword(ugCallBackCancelReq.getApiPassword())){
            ugCallBackCancelResp.setCode("000010");
            ugCallBackCancelResp.setMsg("API PASSWORD ERROR");
        }
        List<UgCallBackCancelItemReq> ugCallBackCancelItemReqList = ugCallBackCancelReq.getData();
        List<UgCallBackCancelSubResp> ugCallBackCancelSubRespList = new ArrayList<>();
        for(UgCallBackCancelItemReq ugCallBackCancelItemReq : ugCallBackCancelItemReqList) {
            UgCallBackCancelSubResp ugCallBackCancelSubResp = new UgCallBackCancelSubResp();
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ugCallBackCancelItemReq.getUserId());

            ugCallBackCancelSubResp.setUserId(ugCallBackCancelItemReq.getUserId());
            ugCallBackCancelSubResp.setTxnId(ugCallBackCancelItemReq.getTxnId());
            if (null == memBaseinfo) {
                ugCallBackCancelSubResp.setCode("100001");
                ugCallBackCancelSubResp.setMsg("MEMBER ACCOUNT IS NOT EXIST");
            } else {
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, ugCallBackCancelItemReq.getTicketId());
                wrapper.eq(Txns::getRoundId,ugCallBackCancelItemReq.getTxnId());
                wrapper.eq(Txns::getUserId, ugCallBackCancelItemReq.getUserId());
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                BigDecimal balance = memBaseinfo.getBalance();
                if (null != oldTxns && !oldTxns.getMethod().equals("Cancel Bet")) {
                    BigDecimal betAmount = oldTxns.getWinAmount().abs();
                    if (oldTxns.getMethod().equals("Place Bet")) {//此交易是否是投注
                        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                        balance = balance.add(betAmount);
                    } else {
                        if (BigDecimal.valueOf(0).compareTo(oldTxns.getWinAmount()) == -1) {
                            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.UNSETTLE, TradingEnum.INCOME);
                            balance = balance.add(betAmount);

                        } else {
                            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
                            balance = balance.subtract(betAmount);
                        }
                    }
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    oldTxns.setStatus("Cancel");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setId(null);
                    txns.setMethod("Cancel Bet");

                    txns.setCreateTime(dateStr);
                    txns.setBalance(balance);
                    txns.setStatus("Running");
                    txnsMapper.insert(txns);
                }
                ugCallBackCancelSubResp.setCode("000010");
                ugCallBackCancelSubResp.setMsg("API PASSWORD ERROR");

            }
            ugCallBackCancelSubRespList.add(ugCallBackCancelSubResp);
        }
        ugCallBackCancelResp.setData(ugCallBackCancelSubRespList);
        return ugCallBackCancelResp;

    }


    //检查交易结果
    public Object check(UgCallBackCheckTxnReq<UgCallBackCheckTxnItemReq> ugCallBackCheckTxnReq) {


        UgCallBackCheckTxnResp ugCallBackCheckTxnResp = new UgCallBackCheckTxnResp();
        ugCallBackCheckTxnResp.setCode("000000");
        ugCallBackCheckTxnResp.setMsg("SUCCESS");
        if(!this.chechkApiPassword(ugCallBackCheckTxnReq.getApiPassword())){
            ugCallBackCheckTxnResp.setCode("000010");
            ugCallBackCheckTxnResp.setMsg("API PASSWORD ERROR");
        }
        List<UgCallBackCheckTxnlSubResp> ugCallBackCheckTxnlSubRespList = new ArrayList<>();
        for (UgCallBackCheckTxnItemReq ugCallBackCheckTxnItemReq : ugCallBackCheckTxnReq.getData()) {
            UgCallBackCheckTxnlSubResp ugCallBackCheckTxnlSubResp = new UgCallBackCheckTxnlSubResp();
            ugCallBackCheckTxnlSubResp.setCode("000000");
            ugCallBackCheckTxnlSubResp.setMsg("SUCCESS");
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ugCallBackCheckTxnItemReq.getUserId());
            ugCallBackCheckTxnlSubResp.setUserId(ugCallBackCheckTxnItemReq.getUserId());
            ugCallBackCheckTxnlSubResp.setTxnId(ugCallBackCheckTxnItemReq.getTxnId());
            if (null == memBaseinfo) {
                ugCallBackCheckTxnlSubResp.setCode("100001");
                ugCallBackCheckTxnlSubResp.setMsg("MEMBER ACCOUNT IS NOT EXIST");
            } else {
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getRoundId,ugCallBackCheckTxnItemReq.getTxnId());
                wrapper.eq(Txns::getUserId, ugCallBackCheckTxnItemReq.getUserId());
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null == oldTxns) {
                    ugCallBackCheckTxnlSubResp.setCode("300002");
                    ugCallBackCheckTxnlSubResp.setMsg("DEPOSIT/WITHDRAWAL FAILED");
                }
                ugCallBackCheckTxnlSubResp.setBalance(memBaseinfo.getBalance());
            }
            ugCallBackCheckTxnlSubRespList.add(ugCallBackCheckTxnlSubResp);
        }
        ugCallBackCheckTxnResp.setData(ugCallBackCheckTxnlSubRespList);
        return ugCallBackCheckTxnResp;

    }

    public boolean chechkApiPassword(String apiPassword){
        return true;
//        if(OpenAPIProperties.UG_API_KEY.equals(apiPassword)){
//            return true;
//        }else {
//            return false;
//        }
    }
}
