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

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public Object getBalance(@RequestParam Map<String, String> params) {
        logger.info("ugCallBack {} callBack 回调,getBalance获取余额 params:{}",JSONObject.toJSONString(params));
        Set<String> keySet = params.keySet();
        UgCallBackGetBalanceReq ugCallBackGetBalanceReq = new UgCallBackGetBalanceReq();
        for(String key : keySet){
            ugCallBackGetBalanceReq = JSONObject.parseObject(key,ugCallBackGetBalanceReq.getClass());
        }
        Object getBalance = ugCallbackService.getBalance(ugCallBackGetBalanceReq);
        logger.info("ugCallBack {} callBack 回调返回数据,getBalance获取余额 params:{}",JSONObject.toJSONString(getBalance));
        return getBalance;
    }

    /**
     * 加余额/扣除余额
     */
    @RequestMapping(value="/Transfer",method=RequestMethod.POST)
    @ResponseBody
    public Object transfer(@RequestParam Map<String, String> params) {
        logger.info("ugCallBack {} callBack 回调,transfer加余额/扣除余额 params:{}",JSONObject.toJSONString(params));
        Set<String> keySet = params.keySet();
        UgCallBackTransferReq<UgCallBackTransactionItemReq> ugCallBackTransferReq = new UgCallBackTransferReq<UgCallBackTransactionItemReq>();
        for(String key : keySet){
            ugCallBackTransferReq = JSONObject.parseObject(key,ugCallBackTransferReq.getClass());
        }
        Object transfer = ugCallbackService.transfer(ugCallBackTransferReq);
        logger.info("ugCallBack {} callBack 回调返回数据,transfer加余额/扣除余额 params:{}",JSONObject.toJSONString(transfer));
        return transfer;
    }

    /**
     * 取消交易
     */
    @RequestMapping(value="/Cancel",method=RequestMethod.POST)
    @ResponseBody
    public Object cancel(@RequestParam Map<String, String> params) {
        logger.info("ugCallBack {} callBack 回调,cancel取消交易 params:{}",JSONObject.toJSONString(params));
        Set<String> keySet = params.keySet();
        UgCallBackCancelReq ugCallBackCancelReq = new UgCallBackCancelReq();
        for(String key : keySet){
            ugCallBackCancelReq = JSONObject.parseObject(key,ugCallBackCancelReq.getClass());
        }
        Object cancel = ugCallbackService.cancel(ugCallBackCancelReq);
        logger.info("ugCallBack {} callBack 回调返回数据,cancel取消交易 params:{}",JSONObject.toJSONString(cancel));
        return cancel;
    }

    /**
     * 检查交易结果
     */
    @RequestMapping(value="/Check",method=RequestMethod.POST)
    @ResponseBody
    public Object check(@RequestParam Map<String, String> params) {
        logger.info("ugCallBack {} callBack 回调,check检查交易结果 params:{}",JSONObject.toJSONString(params));
        Set<String> keySet = params.keySet();
        UgCallBackCancelReq ugCallBackCancelReq = new UgCallBackCancelReq();
        for(String key : keySet){
            ugCallBackCancelReq = JSONObject.parseObject(key,ugCallBackCancelReq.getClass());
        }
        Object check = ugCallbackService.check(ugCallBackCancelReq);
        logger.info("ugCallBack {} callBack 回调返回数据,check检查交易结果 params:{}",JSONObject.toJSONString(check));
        return check;
    }
}
