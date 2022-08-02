package com.indo.game.controller.sbo;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.sbo.*;
import com.indo.game.service.sbo.SboCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sbo/callBack")
public class SboCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SboCallbackService sboCallbackService;


    /**
     * 取得用户的余额
     */
    @RequestMapping(value="/GetBalance",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sboCallBack GetBalance 回调,取得用户的余额 params:{} IP:{}",jsonObject,ip);
        SboCallBackParentReq sboCallBackParentReq = JSONObject.toJavaObject(jsonObject,SboCallBackParentReq.class);
        Object getBalance = sboCallbackService.getBalance(sboCallBackParentReq,ip);
        logger.info("sboCallBack  GetBalance 回调返回数据,取得用户的余额 params:{}",getBalance);
        return getBalance;
    }

    /**
     * 扣除投注金额
     */
    @RequestMapping(value="/Deduct",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object deduct(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sboCallBack  Deduct 回调,扣除投注金额 params:{} IP:{}",jsonObject,ip);
        SboCallBackDeductReq sboCallBackDeductReq = JSONObject.toJavaObject(jsonObject,SboCallBackDeductReq.class);
        Object deduct = sboCallbackService.deduct(sboCallBackDeductReq,ip);
        logger.info("sboCallBack Deduct 回调返回数据,扣除投注金额 params:{}",deduct);
        return deduct;
    }

    /**
     * 结算投注
     */
    @RequestMapping(value="/Settle",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object settle(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sboCallBack settle 回调,结算投注 params:{} IP:{}",jsonObject,ip);
        SboCallBackSettleReq sboCallBackSettleReq = JSONObject.toJavaObject(jsonObject,SboCallBackSettleReq.class);
        Object settle = sboCallbackService.settle(sboCallBackSettleReq,ip);
        logger.info("sboCallBack settle 回调返回数据,结算投注 params:{}",settle);
        return settle;
    }

    /**
     * 回滚
     */
    @RequestMapping(value="/Rollback",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object rollback(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sboCallBack rollback 回调,回滚 params:{} IP:{}",jsonObject,ip);
        SboCallBackRollbackReq sboCallBackRollbackReq = JSONObject.toJavaObject(jsonObject,SboCallBackRollbackReq.class);
        Object rollback = sboCallbackService.rollback(sboCallBackRollbackReq,ip);
        logger.info("sboCallBack rollback 回调返回数据,回滚 params:{}",rollback);
        return rollback;
    }

    /**
     * 取消投注
     */
    @RequestMapping(value="/Cancel",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object cancel(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sboCallBack Cancel 回调,回滚 params:{} IP:{}",jsonObject,ip);
        SboCallBackCancelReq sboCallBackCancelReq = JSONObject.toJavaObject(jsonObject,SboCallBackCancelReq.class);
        Object cancel = sboCallbackService.cancel(sboCallBackCancelReq,ip);
        logger.info("sboCallBack Cancel 回调返回数据,回滚 params:{}",cancel);
        return cancel;
    }

    /**
     * 小费
     */
    @RequestMapping(value="/Tip",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object tip(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sboCallBack tip 回调,小费 params:{} IP:{}",jsonObject,ip);
        SboCallBackTipReq sboCallBackTipReq = JSONObject.toJavaObject(jsonObject,SboCallBackTipReq.class);
        Object tip = sboCallbackService.tip(sboCallBackTipReq,ip);
        logger.info("sboCallBack tip 回调返回数据,小费 params:{}",tip);
        return tip;
    }

    /**
     * 红利
     */
    @RequestMapping(value="/Bonus",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object bonus(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sboCallBack  bonus 回调,红利 params:{} IP:{}",jsonObject,ip);
        SboCallBackBonusReq sboCallBackBonusReq = JSONObject.toJavaObject(jsonObject,SboCallBackBonusReq.class);
        Object bonus = sboCallbackService.bonus(sboCallBackBonusReq,ip);
        logger.info("sboCallBack  bonus 回调返回数据,红利 params:{}",bonus);
        return bonus;
    }

    /**
     * 归还注额
     */
    @RequestMapping(value="/ReturnStake",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object returnStake(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sboCallBack returnStake 回调,归还注额 params:{} IP:{}",jsonObject,ip);
        SboCallBackReturnStakeReq sboCallBackBonusReq = JSONObject.toJavaObject(jsonObject,SboCallBackReturnStakeReq.class);
        Object returnStake = sboCallbackService.returnStake(sboCallBackBonusReq,ip);
        logger.info("sboCallBack returnStake 回调返回数据,归还注额 params:{}",returnStake);
        return returnStake;
    }

    /**
     * 取得投注状态
     */
    @RequestMapping(value="/GetBetStatus",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBetStatus(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sboCallBack getBetStatus 回调,取得投注状态 params:{} IP:{}",jsonObject,ip);
        SboCallBackGetBetStatusReq sboCallBackGetBetStatusReq = JSONObject.toJavaObject(jsonObject,SboCallBackGetBetStatusReq.class);
        Object getBetStatus = sboCallbackService.getBetStatus(sboCallBackGetBetStatusReq,ip);
        logger.info("sboCallBack  getBetStatus 回调返回数据,取得投注状态 params:{}",getBetStatus);
        return getBetStatus;
    }

//    /**
//     * 转帐交易
//     */
//    @RequestMapping(value="/Transfer",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public Object transfer(SboCallBackTransferReq sboCallBackTransferReq) {
//        logger.info("sboCallBack {} transfer 回调,取得投注状态 params:{}",JSONObject.toJSONString(sboCallBackTransferReq));
//        Object transfer = sboCallbackService.transfer(sboCallBackTransferReq);
//        logger.info("sboCallBack {} transfer 回调返回数据,取得投注状态 params:{}",transfer);
//        return transfer;
//    }
//
//    /**
//     * 转帐交易回滚
//     */
//    @RequestMapping(value="/RollbackTransfer",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public Object rollbackTransfer(SboCallBackRollbackTransferReq sboCallBackRollbackTransferReq) {
//        logger.info("sboCallBack {} rollbackTransfer 回调,转帐交易回滚 params:{}",JSONObject.toJSONString(sboCallBackRollbackTransferReq));
//        Object rollbackTransfer = sboCallbackService.rollbackTransfer(sboCallBackRollbackTransferReq);
//        logger.info("sboCallBack {} rollbackTransfer 回调返回数据,转帐交易回滚 params:{}",rollbackTransfer);
//        return rollbackTransfer;
//    }
//
//    /**
//     * 取得转帐交易状态
//     */
//    @RequestMapping(value="/GetTransferStatus",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public Object getTransferStatus(SboCallBackGetTransferStautsReq sboCallBackGetTransferStautsReq) {
//        logger.info("sboCallBack {} getTransferStatus 回调,取得转帐交易状态 params:{}",JSONObject.toJSONString(sboCallBackGetTransferStautsReq));
//        Object getTransferStatus = sboCallbackService.getTransferStatus(sboCallBackGetTransferStautsReq);
//        logger.info("sboCallBack {} getTransferStatus 回调返回数据,取得转帐交易状态 params:{}",getTransferStatus);
//        return getTransferStatus;
//    }

    /**
     * LiveCoin購買
     */
    @RequestMapping(value="/liveCoinTransaction",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object liveCoinTransaction(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sboCallBack  getTransferStatus 回调,LiveCoin購買 params:{} IP:{}",jsonObject,ip);
        SboCallBackLiveCoinTransactionReq sboCallBackLiveCoinTransactionReq = JSONObject.toJavaObject(jsonObject,SboCallBackLiveCoinTransactionReq.class);
        Object liveCoinTransaction = sboCallbackService.liveCoinTransaction(sboCallBackLiveCoinTransactionReq,ip);
        logger.info("sboCallBack  getTransferStatus 回调返回数据,LiveCoin購買 params:{}",liveCoinTransaction);
        return liveCoinTransaction;
    }
}
