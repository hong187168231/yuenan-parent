package com.indo.game.controller.ug;

import com.alibaba.fastjson.JSONObject;
import com.indo.game.pojo.entity.ug.UgCallBackCancelReq;
import com.indo.game.pojo.entity.ug.UgCallBackGetBalanceReq;
import com.indo.game.pojo.entity.ug.UgCallBackTransactionItemReq;
import com.indo.game.pojo.entity.ug.UgCallBackTransferReq;
import com.indo.game.service.ug.UgCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ug")
public class UgCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UgCallbackService ugCallbackService;

    /**
     * 获取余额
     */
    @RequestMapping(value="/GetBalance",method=RequestMethod.POST)
    public String getBalance(UgCallBackGetBalanceReq ugCallBackGetBalanceReq) {
        logger.info("ugCallBack {} callBack 回调,getBalance获取余额 params:{}",JSONObject.toJSONString(ugCallBackGetBalanceReq));
        String getBalance = ugCallbackService.getBalance(ugCallBackGetBalanceReq);
        logger.info("ugCallBack {} callBack 回调返回数据,getBalance获取余额 params:{}",ugCallBackGetBalanceReq);
        return getBalance;
    }

    /**
     * 加余额/扣除余额
     */
    @RequestMapping(value="/Transfer",method=RequestMethod.POST)
    public String transfer(UgCallBackTransferReq<UgCallBackTransactionItemReq> ugCallBackTransactionItemReqUgCallBackTransferReq) {
        logger.info("ugCallBack {} callBack 回调,transfer加余额/扣除余额 params:{}",JSONObject.toJSONString(ugCallBackTransactionItemReqUgCallBackTransferReq));
        String transfer = ugCallbackService.transfer(ugCallBackTransactionItemReqUgCallBackTransferReq);
        logger.info("ugCallBack {} callBack 回调返回数据,transfer加余额/扣除余额 params:{}",transfer);
        return transfer;
    }

    /**
     * 取消交易
     */
    @RequestMapping(value="/Cancel",method=RequestMethod.POST)
    public String cancel(UgCallBackCancelReq ugCallBackCancelReq) {
        logger.info("ugCallBack {} callBack 回调,cancel取消交易 params:{}",JSONObject.toJSONString(ugCallBackCancelReq));
        String cancel = ugCallbackService.cancel(ugCallBackCancelReq);
        logger.info("ugCallBack {} callBack 回调返回数据,cancel取消交易 params:{}",cancel);
        return cancel;
    }

    /**
     * 检查交易结果
     */
    @RequestMapping(value="/Check",method=RequestMethod.POST)
    public String check(UgCallBackCancelReq ugCallBackCancelReq) {
        logger.info("ugCallBack {} callBack 回调,check检查交易结果 params:{}",JSONObject.toJSONString(ugCallBackCancelReq));
        String check = ugCallbackService.check(ugCallBackCancelReq);
        logger.info("ugCallBack {} callBack 回调返回数据,check检查交易结果 params:{}",check);
        return check;
    }
}
