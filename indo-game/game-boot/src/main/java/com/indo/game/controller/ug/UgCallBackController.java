package com.indo.game.controller.ug;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.game.pojo.dto.ug.*;
import com.indo.game.service.ug.UgCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/ug/callBack")
public class UgCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UgCallbackService ugCallbackService;

    /**
     * 玩家登入验证URL
     */
    @RequestMapping(value="/checkLogin",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object checkLogin(@RequestBody JSONObject jsonObject) {
        logger.info("ugCallBack checkLogin 回调玩家登入验证URL,checkLogin params:{}",JSONObject.toJSONString(jsonObject));
        UgCallBackCheckLoginReq ugCallBackCheckLoginReq = new UgCallBackCheckLoginReq();
        ugCallBackCheckLoginReq.setApiPassword(jsonObject.getString("apiPassword"));
        ugCallBackCheckLoginReq.setUserId(jsonObject.getString("userId"));
        ugCallBackCheckLoginReq.setToken(jsonObject.getString("token"));
        Object getBalance = ugCallbackService.checkLogin(ugCallBackCheckLoginReq);
        logger.info("ugCallBack checkLogin 回调玩家登入验证URL返回数据,checkLogin params:{}",JSONObject.toJSONString(getBalance));
        return getBalance;
    }
    /**
     * 获取余额
     */
    @RequestMapping(value="/getBalance",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(@RequestBody JSONObject jsonObject) {
        logger.info("ugCallBack getBalance 回调,getBalance获取余额 params:{}",JSONObject.toJSONString(jsonObject));
        UgCallBackGetBalanceReq ugCallBackGetBalanceReq = new UgCallBackGetBalanceReq();
        ugCallBackGetBalanceReq.setApiPassword(jsonObject.getString("apiPassword"));
        ugCallBackGetBalanceReq.setUserId(jsonObject.getString("userId"));
        Object getBalance = ugCallbackService.getBalance(ugCallBackGetBalanceReq);
        logger.info("ugCallBack  getBalance 回调返回数据,getBalance获取余额 params:{}",JSONObject.toJSONString(getBalance));
        return getBalance;
    }

    /**
     * 加余额/扣除余额
     */
    @RequestMapping(value="/changeBalance",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object transfer(@RequestBody JSONObject jsonObject) {
        logger.info("ugCallBack  transfer 回调,transfer加余额/扣除余额 params:{}",JSONObject.toJSONString(jsonObject));
        UgCallBackTransferReq<UgCallBackTransactionItemReq> ugCallBackTransferReq = JSONObject.toJavaObject(jsonObject,UgCallBackTransferReq.class);
        Object transfer = ugCallbackService.transfer(ugCallBackTransferReq);
        logger.info("ugCallBack transfer 回调返回数据,transfer加余额/扣除余额 params:{}",JSONObject.toJSONString(transfer));
        return transfer;
    }

    /**
     * 取消交易
     */
    @RequestMapping(value="/cancelBet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object cancel(@RequestBody JSONObject jsonObject) {
        logger.info("ugCallBack  cancel 回调,cancel取消交易 params:{}",JSONObject.toJSONString(jsonObject));
        UgCallBackCancelReq<UgCallBackCancelItemReq> ugCallBackCancelReq = JSONObject.toJavaObject(jsonObject,UgCallBackCancelReq.class);

        Object cancel = ugCallbackService.cancel(ugCallBackCancelReq);
        logger.info("ugCallBack   cancel 回调返回数据,cancel取消交易 params:{}",JSONObject.toJSONString(cancel));
        return cancel;
    }

    /**
     * 检查交易结果
     */
    @RequestMapping(value="/checkTxn",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object check(@RequestBody JSONObject jsonObject) {
        logger.info("ugCallBack  check 回调,check检查交易结果 params:{}",JSONObject.toJSONString(jsonObject));
        UgCallBackCheckTxnReq<UgCallBackCheckTxnItemReq> ugCallBackCheckTxnReq = JSONObject.toJavaObject(jsonObject,UgCallBackCheckTxnReq.class);
        Object check = ugCallbackService.check(ugCallBackCheckTxnReq);
        logger.info("ugCallBack  check 回调返回数据,check检查交易结果 params:{}",JSONObject.toJSONString(check));
        return check;
    }
}
