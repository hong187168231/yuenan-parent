package com.indo.game.controller.ug;

import com.alibaba.fastjson.JSONObject;
import com.indo.game.pojo.dto.ug.UgCallBackCancelReq;
import com.indo.game.pojo.dto.ug.UgCallBackGetBalanceReq;
import com.indo.game.pojo.dto.ug.UgCallBackTransactionItemReq;
import com.indo.game.pojo.dto.ug.UgCallBackTransferReq;
import com.indo.game.service.ug.UgCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public Object getBalance(@RequestParam UgCallBackGetBalanceReq ugCallBackGetBalanceReq) {
        logger.info("ugCallBack {} callBack 回调,getBalance获取余额 params:{}",JSONObject.toJSONString(ugCallBackGetBalanceReq));
        Object getBalance = ugCallbackService.getBalance(ugCallBackGetBalanceReq);
        logger.info("ugCallBack {} callBack 回调返回数据,getBalance获取余额 params:{}",ugCallBackGetBalanceReq);
        return getBalance;
    }

    /**
     * 加余额/扣除余额
     */
    @RequestMapping(value="/Transfer",method=RequestMethod.POST)
    @ResponseBody
    public Object transfer(@RequestParam UgCallBackTransferReq<UgCallBackTransactionItemReq> ugCallBackTransactionItemReqUgCallBackTransferReq) {
        logger.info("ugCallBack {} callBack 回调,transfer加余额/扣除余额 params:{}",JSONObject.toJSONString(ugCallBackTransactionItemReqUgCallBackTransferReq));
        Object transfer = ugCallbackService.transfer(ugCallBackTransactionItemReqUgCallBackTransferReq);
        logger.info("ugCallBack {} callBack 回调返回数据,transfer加余额/扣除余额 params:{}",transfer);
        return transfer;
    }

    /**
     * 取消交易
     */
    @RequestMapping(value="/Cancel",method=RequestMethod.POST)
    @ResponseBody
    public Object cancel(@RequestParam UgCallBackCancelReq ugCallBackCancelReq) {
        logger.info("ugCallBack {} callBack 回调,cancel取消交易 params:{}",JSONObject.toJSONString(ugCallBackCancelReq));
        Object cancel = ugCallbackService.cancel(ugCallBackCancelReq);
        logger.info("ugCallBack {} callBack 回调返回数据,cancel取消交易 params:{}",cancel);
        return cancel;
    }

    /**
     * 检查交易结果
     */
    @RequestMapping(value="/Check",method=RequestMethod.POST)
    @ResponseBody
    public Object check(@RequestParam UgCallBackCancelReq ugCallBackCancelReq) {
        logger.info("ugCallBack {} callBack 回调,check检查交易结果 params:{}",JSONObject.toJSONString(ugCallBackCancelReq));
        Object check = ugCallbackService.check(ugCallBackCancelReq);
        logger.info("ugCallBack {} callBack 回调返回数据,check检查交易结果 params:{}",check);
        return check;
    }
}
