package com.indo.game.service.awc.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.utils.DateUtils;
import com.indo.game.pojo.entity.awc.*;
import com.indo.game.pojo.vo.callback.CallBackFail;
import com.indo.game.pojo.vo.callback.CallBackParentSuccess;
import com.indo.game.pojo.vo.callback.GetBalanceSuccess;
import com.indo.game.pojo.vo.callback.CallBackSuccess;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.awc.AwcCallbackService;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
public class AwcCallbackServiceImpl implements AwcCallbackService {

    @Autowired
    private GameCommonService gameCommonService;

    public String awcAeSexybcrtCallback(AwcApiRequestParentData awcApiRequestData) {
        Map<String, Object> params = (Map<String, Object>) awcApiRequestData.getMessage();
        String action = params.get("action").toString();
        //Get Balance 取得玩家余额
        if ("getBalance".equals(action)) {
            return getBalance(params);
        }
        //Place Bet 下注
        if ("bet".equals(action)) {
            return bet(awcApiRequestData);
        }
        //Cancel Bet 取消注单
        if ("cancelBet".equals(action)) {
            return cancelBet(awcApiRequestData);
        }
        //Adjust Bet 调整投注
        if ("adjustBet".equals(action)) {
            return adjustBet(awcApiRequestData);
        }
        //Void Bet 交易作废
        if ("voidBet".equals(action)) {
            return voidBet(awcApiRequestData);
        }
        //Unvoid Bet 取消交易作废
        if ("unvoidBet".equals(action)) {
            return unvoidBet(awcApiRequestData);
        }
        //Refund 返还金额
        if ("refund".equals(action)) {
            return refund(awcApiRequestData);
        }
        //Settle 已结帐派彩
        if ("settle".equals(action)) {
            return settle(awcApiRequestData);
        }
        //Unsettle 取消结帐派彩
        if ("unsettle".equals(action)) {
            return unsettle(awcApiRequestData);
        }
        //Void Settle 结帐单转为无效
        if ("voidSettle".equals(action)) {
            return voidSettle(awcApiRequestData);
        }
        //Unvoid Settle 无效单结账
        if ("unvoidSettle".equals(action)) {
            return unvoidSettle(awcApiRequestData);
        }
        // BetNSettle 下注并直接结算
        if ("betNSettle".equals(action)) {
            return betNSettle(awcApiRequestData);
        }
        // Cancel BetNSettle 取消结算并取消注单
        if ("cancelBetNSettle".equals(action)) {
            return cancelBetNSettle(awcApiRequestData);
        }
        // Free Spin 免费旋转
        if ("freeSpin".equals(action)) {
            return freeSpin(awcApiRequestData);
        }
        //  Give (Promotion Bonus) 活动派彩
        if ("give".equals(action)) {
            return give(awcApiRequestData);
        }
        //  Tip 打赏
        if ("tip".equals(action)) {
            return tip(awcApiRequestData);
        }
        //  Cancel Tip 取消打赏
        if ("cancelTip".equals(action)) {
            return cancelTip(awcApiRequestData);
        }
        return "";
    }

    //Get Balance 取得玩家余额
    private String getBalance(Map<String, Object> params) {
        String userId = params.get("userId").toString();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        BigDecimal balance = memBaseinfo.getBalance();
        if (null == balance) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            GetBalanceSuccess getBalanceSuccess = new GetBalanceSuccess();
            getBalanceSuccess.setStatus("0000");
            getBalanceSuccess.setBalance(balance.toString());
            getBalanceSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            getBalanceSuccess.setUserId(userId);
            return JSONObject.toJSONString(getBalanceSuccess);
        }


    }


    //Place Bet 下注
    private String bet(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<PlaceBetTxns> apiRequestData = (AwcApiRequestData<PlaceBetTxns>) awcApiRequestData.getMessage();
        PlaceBetTxns placeBetTxns = apiRequestData.getTxns();
        String userId = placeBetTxns.getUserId();

        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackSuccess placeBetSuccess = new CallBackSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(userId);
            placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            return JSONObject.toJSONString(placeBetSuccess);
        }
//        gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.AWCAESEXYBCRT_IN.getValue(), balance, memBaseinfo, content, cptOpenMember, Constants.AWC_AESEXYBCRT_ACCOUNT_TYPE);
    }

    //Cancel Bet 取消注单
    private String cancelBet(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<CancelBetTxns> apiRequestData = (AwcApiRequestData<CancelBetTxns>) awcApiRequestData.getMessage();
        CancelBetTxns cancelBetTxns = apiRequestData.getTxns();
        String userId = cancelBetTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackSuccess placeBetSuccess = new CallBackSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(userId);
            placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            return JSONObject.toJSONString(placeBetSuccess);
        }
    }

    //Adjust Bet 调整投注
    private String adjustBet(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<AdjustBetTxns> apiRequestData = (AwcApiRequestData<AdjustBetTxns>) awcApiRequestData.getMessage();
        AdjustBetTxns adjustBetTxns = apiRequestData.getTxns();
        String userId = adjustBetTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackSuccess placeBetSuccess = new CallBackSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(userId);
            placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            return JSONObject.toJSONString(placeBetSuccess);
        }
    }

    //Void Bet 交易作废
    private String voidBet(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<VoidBetTxns> apiRequestData = (AwcApiRequestData<VoidBetTxns>) awcApiRequestData.getMessage();
        VoidBetTxns voidBetTxns = apiRequestData.getTxns();
        String userId = voidBetTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackParentSuccess callBackSuccess = new CallBackParentSuccess();
            callBackSuccess.setStatus("0000");
            return JSONObject.toJSONString(callBackSuccess);
        }
    }

    //Unvoid Bet 取消交易作废
    private String unvoidBet(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<UnvoidBetTxns> apiRequestData = (AwcApiRequestData<UnvoidBetTxns>) awcApiRequestData.getMessage();
        UnvoidBetTxns unvoidBetTxns = apiRequestData.getTxns();
        String userId = unvoidBetTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackParentSuccess callBackSuccess = new CallBackParentSuccess();
            callBackSuccess.setStatus("0000");
            return JSONObject.toJSONString(callBackSuccess);
        }
    }

    //Refund 返还金额
    private String refund(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<RefundTxns> apiRequestData = (AwcApiRequestData<RefundTxns>) awcApiRequestData.getMessage();
        RefundTxns refundTxns = apiRequestData.getTxns();
        String userId = refundTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackParentSuccess callBackSuccess = new CallBackParentSuccess();
            callBackSuccess.setStatus("0000");
            return JSONObject.toJSONString(callBackSuccess);
        }
    }

    //Settle 已结帐派彩
    private String settle(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<SettleTxns> apiRequestData = (AwcApiRequestData<SettleTxns>) awcApiRequestData.getMessage();
        SettleTxns settleTxns = apiRequestData.getTxns();
        String userId = settleTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackParentSuccess callBackSuccess = new CallBackParentSuccess();
            callBackSuccess.setStatus("0000");
            return JSONObject.toJSONString(callBackSuccess);
        }
    }

    //Unsettle 取消结帐派彩
    private String unsettle(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<UnsettleTxns> apiRequestData = (AwcApiRequestData<UnsettleTxns>) awcApiRequestData.getMessage();
        UnsettleTxns unsettleTxns = apiRequestData.getTxns();
        String userId = unsettleTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackParentSuccess callBackSuccess = new CallBackParentSuccess();
            callBackSuccess.setStatus("0000");
            return JSONObject.toJSONString(callBackSuccess);
        }
    }

    //Void Settle 结帐单转为无效
    private String voidSettle(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<VoidSettleTxns> apiRequestData = (AwcApiRequestData<VoidSettleTxns>) awcApiRequestData.getMessage();
        VoidSettleTxns voidSettleTxns = apiRequestData.getTxns();
        String userId = voidSettleTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackParentSuccess callBackSuccess = new CallBackParentSuccess();
            callBackSuccess.setStatus("0000");
            return JSONObject.toJSONString(callBackSuccess);
        }
    }

    //Unvoid Settle 无效单结账
    private String unvoidSettle(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<UnvoidSettleTxns> apiRequestData = (AwcApiRequestData<UnvoidSettleTxns>) awcApiRequestData.getMessage();
        UnvoidSettleTxns unvoidSettleTxns = apiRequestData.getTxns();
        String userId = unvoidSettleTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackSuccess placeBetSuccess = new CallBackSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(userId);
            placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            return JSONObject.toJSONString(placeBetSuccess);
        }
    }

    // BetNSettle 下注并直接结算
    private String betNSettle(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<BetNSettleTxns> apiRequestData = (AwcApiRequestData<BetNSettleTxns>) awcApiRequestData.getMessage();
        BetNSettleTxns betNSettleTxns = apiRequestData.getTxns();
        String userId = betNSettleTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackSuccess placeBetSuccess = new CallBackSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(userId);
            placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            return JSONObject.toJSONString(placeBetSuccess);
        }
    }

    // Cancel BetNSettle 取消结算并取消注单
    private String cancelBetNSettle(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<CancelBetNSettleTxns> apiRequestData = (AwcApiRequestData<CancelBetNSettleTxns>) awcApiRequestData.getMessage();
        CancelBetNSettleTxns cancelBetNSettleTxns = apiRequestData.getTxns();
        String userId = cancelBetNSettleTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackSuccess placeBetSuccess = new CallBackSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(userId);
            placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            return JSONObject.toJSONString(placeBetSuccess);
        }
    }

    // Free Spin 免费旋转
    private String freeSpin(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<FreeSpinTxns> apiRequestData = (AwcApiRequestData<FreeSpinTxns>) awcApiRequestData.getMessage();
        FreeSpinTxns freeSpinTxns = apiRequestData.getTxns();
        String userId = freeSpinTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackParentSuccess callBackSuccess = new CallBackParentSuccess();
            callBackSuccess.setStatus("0000");
            return JSONObject.toJSONString(callBackSuccess);
        }
    }

    //  Give (Promotion Bonus) 活动派彩
    private String give(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<GiveTxns> apiRequestData = (AwcApiRequestData<GiveTxns>) awcApiRequestData.getMessage();
        GiveTxns giveTxns = apiRequestData.getTxns();
        String userId = giveTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackSuccess placeBetSuccess = new CallBackSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(userId);
            placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            return JSONObject.toJSONString(placeBetSuccess);
        }
    }

    //  Tip 打赏
    private String tip(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<TipTxns> apiRequestData = (AwcApiRequestData<TipTxns>) awcApiRequestData.getMessage();
        TipTxns tipTxns = apiRequestData.getTxns();
        String userId = tipTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackSuccess placeBetSuccess = new CallBackSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(userId);
            placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            return JSONObject.toJSONString(placeBetSuccess);
        }
    }

    //  Cancel Tip 取消打赏
    private String cancelTip(AwcApiRequestParentData awcApiRequestData) {
        AwcApiRequestData<CancelTipTxns> apiRequestData = (AwcApiRequestData<CancelTipTxns>) awcApiRequestData.getMessage();
        CancelTipTxns cancelTipTxns = apiRequestData.getTxns();
        String userId = cancelTipTxns.getUserId();
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(userId);
        if (null == memBaseinfo) {
            CallBackFail callBacekFail = new CallBackFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            CallBackSuccess placeBetSuccess = new CallBackSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(userId);
            placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            return JSONObject.toJSONString(placeBetSuccess);
        }
    }


}
