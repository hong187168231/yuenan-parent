package com.indo.game.controller.sbo;

import com.alibaba.fastjson.JSONObject;
import com.indo.game.pojo.entity.sbo.*;
import com.indo.game.service.sbo.SboCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sbo")
public class SboCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SboCallbackService sboCallbackService;


    /**
     * 取得用户的余额
     */
    @RequestMapping(value="/GetBalance",method=RequestMethod.POST)
    public String getBalance(SboCallBackParentReq sboCallBackParentReq) {
        logger.info("sboCallBack {} GetBalance 回调,取得用户的余额 params:{}",JSONObject.toJSONString(sboCallBackParentReq));
        String getBalance = sboCallbackService.getBalance(sboCallBackParentReq);
        logger.info("sboCallBack {} GetBalance 回调返回数据,取得用户的余额 params:{}",getBalance);
        return getBalance;
    }

    /**
     * 扣除投注金额
     */
    @RequestMapping(value="/Deduct",method=RequestMethod.POST)
    public String deduct(SboCallBackDeductReq sboCallBackDeductReq) {
        logger.info("sboCallBack {} Deduct 回调,扣除投注金额 params:{}",JSONObject.toJSONString(sboCallBackDeductReq));
        String deduct = sboCallbackService.deduct(sboCallBackDeductReq);
        logger.info("sboCallBack {} Deduct 回调返回数据,扣除投注金额 params:{}",deduct);
        return deduct;
    }

    /**
     * 结算投注
     */
    @RequestMapping(value="/Settle",method=RequestMethod.POST)
    public String settle(SboCallBackSettleReq sboCallBackSettleReq) {
        logger.info("sboCallBack {} settle 回调,结算投注 params:{}",JSONObject.toJSONString(sboCallBackSettleReq));
        String settle = sboCallbackService.settle(sboCallBackSettleReq);
        logger.info("sboCallBack {} settle 回调返回数据,结算投注 params:{}",settle);
        return settle;
    }

    /**
     * 回滚
     */
    @RequestMapping(value="/Rollback",method=RequestMethod.POST)
    public String rollback(SboCallBackRollbackReq sboCallBackRollbackReq) {
        logger.info("sboCallBack {} rollback 回调,回滚 params:{}",JSONObject.toJSONString(sboCallBackRollbackReq));
        String rollback = sboCallbackService.rollback(sboCallBackRollbackReq);
        logger.info("sboCallBack {} rollback 回调返回数据,回滚 params:{}",rollback);
        return rollback;
    }

    /**
     * 取消投注
     */
    @RequestMapping(value="/Cancel",method=RequestMethod.POST)
    public String cancel(SboCallBackCancelReq sboCallBackCancelReq) {
        logger.info("sboCallBack {} Cancel 回调,回滚 params:{}",JSONObject.toJSONString(sboCallBackCancelReq));
        String cancel = sboCallbackService.cancel(sboCallBackCancelReq);
        logger.info("sboCallBack {} Cancel 回调返回数据,回滚 params:{}",cancel);
        return cancel;
    }

    /**
     * 小费
     */
    @RequestMapping(value="/Tip",method=RequestMethod.POST)
    public String tip(SboCallBackTipReq sboCallBackTipReq) {
        logger.info("sboCallBack {} tip 回调,小费 params:{}",JSONObject.toJSONString(sboCallBackTipReq));
        String tip = sboCallbackService.tip(sboCallBackTipReq);
        logger.info("sboCallBack {} tip 回调返回数据,小费 params:{}",tip);
        return tip;
    }

    /**
     * 红利
     */
    @RequestMapping(value="/Bonus",method=RequestMethod.POST)
    public String bonus(SboCallBackBonusReq sboCallBackBonusReq) {
        logger.info("sboCallBack {} bonus 回调,红利 params:{}",JSONObject.toJSONString(sboCallBackBonusReq));
        String bonus = sboCallbackService.bonus(sboCallBackBonusReq);
        logger.info("sboCallBack {} bonus 回调返回数据,红利 params:{}",bonus);
        return bonus;
    }

    /**
     * 归还注额
     */
    @RequestMapping(value="/ReturnStake",method=RequestMethod.POST)
    public String returnStake(SboCallBackReturnStakeReq sboCallBackBonusReq) {
        logger.info("sboCallBack {} returnStake 回调,归还注额 params:{}",JSONObject.toJSONString(sboCallBackBonusReq));
        String returnStake = sboCallbackService.returnStake(sboCallBackBonusReq);
        logger.info("sboCallBack {} returnStake 回调返回数据,归还注额 params:{}",returnStake);
        return returnStake;
    }

    /**
     * 取得投注状态
     */
    @RequestMapping(value="/GetBetStatus",method=RequestMethod.POST)
    public String getBetStatus(SboCallBackGetBetStatusReq sboCallBackGetBetStatusReq) {
        logger.info("sboCallBack {} getBetStatus 回调,取得投注状态 params:{}",JSONObject.toJSONString(sboCallBackGetBetStatusReq));
        String getBetStatus = sboCallbackService.getBetStatus(sboCallBackGetBetStatusReq);
        logger.info("sboCallBack {} getBetStatus 回调返回数据,取得投注状态 params:{}",getBetStatus);
        return getBetStatus;
    }

    /**
     * 转帐交易
     */
    @RequestMapping(value="/Transfer",method=RequestMethod.POST)
    public String transfer(SboCallBackTransferReq sboCallBackTransferReq) {
        logger.info("sboCallBack {} transfer 回调,取得投注状态 params:{}",JSONObject.toJSONString(sboCallBackTransferReq));
        String transfer = sboCallbackService.transfer(sboCallBackTransferReq);
        logger.info("sboCallBack {} transfer 回调返回数据,取得投注状态 params:{}",transfer);
        return transfer;
    }

    /**
     * 转帐交易回滚
     */
    @RequestMapping(value="/RollbackTransfer",method=RequestMethod.POST)
    public String rollbackTransfer(SboCallBackRollbackTransferReq sboCallBackRollbackTransferReq) {
        logger.info("sboCallBack {} rollbackTransfer 回调,转帐交易回滚 params:{}",JSONObject.toJSONString(sboCallBackRollbackTransferReq));
        String rollbackTransfer = sboCallbackService.rollbackTransfer(sboCallBackRollbackTransferReq);
        logger.info("sboCallBack {} rollbackTransfer 回调返回数据,转帐交易回滚 params:{}",rollbackTransfer);
        return rollbackTransfer;
    }

    /**
     * 取得转帐交易状态
     */
    @RequestMapping(value="/GetTransferStatus",method=RequestMethod.POST)
    public String getTransferStatus(SboCallBackGetTransferStautsReq sboCallBackGetTransferStautsReq) {
        logger.info("sboCallBack {} getTransferStatus 回调,取得转帐交易状态 params:{}",JSONObject.toJSONString(sboCallBackGetTransferStautsReq));
        String getTransferStatus = sboCallbackService.getTransferStatus(sboCallBackGetTransferStautsReq);
        logger.info("sboCallBack {} getTransferStatus 回调返回数据,取得转帐交易状态 params:{}",getTransferStatus);
        return getTransferStatus;
    }

    /**
     * LiveCoin購買
     */
    @RequestMapping(value="/liveCoinTransaction",method=RequestMethod.POST)
    public String liveCoinTransaction(SboCallBackLiveCoinTransactionReq sboCallBackLiveCoinTransactionReq) {
        logger.info("sboCallBack {} getTransferStatus 回调,LiveCoin購買 params:{}",JSONObject.toJSONString(sboCallBackLiveCoinTransactionReq));
        String liveCoinTransaction = sboCallbackService.liveCoinTransaction(sboCallBackLiveCoinTransactionReq);
        logger.info("sboCallBack {} getTransferStatus 回调返回数据,LiveCoin購買 params:{}",liveCoinTransaction);
        return liveCoinTransaction;
    }
}
