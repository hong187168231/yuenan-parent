package com.indo.game.controller.cq;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.cq.CqBetCallBackReq;
import com.indo.game.pojo.dto.cq.CqEndroundCallBackReq;
import com.indo.game.pojo.dto.cq.CqEndroundDataCallBackReq;
import com.indo.game.service.cq.CqCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cq")
public class CqCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CqCallbackService cqCallbackService;


    /**
     * 查询余额
     */
    @RequestMapping(value = "/callBack/transaction/balance/{account}", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(@PathVariable(name = "account") String account, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  getBalance 回调,params:{},wtoken:{}", JSONObject.toJSONString(account));
        Object object = cqCallbackService.cqBalanceCallback(account, ip, wtoken);
        logger.info("cqCallBack  getBalance 回调下注返回数据,取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 验证用户
     */
    @RequestMapping(value = "/callBack/player/check/{account}", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object checkPlayer(@PathVariable(name = "account") String account, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  checkPlayer回调,params:{},wtoken:{}", JSONObject.toJSONString(account), wtoken);
        Object object = cqCallbackService.cqCheckPlayerCallback(account, ip, wtoken);
        logger.info("cqCallBack  checkPlayer回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 老虎機下注
     */
    @RequestMapping(value = "/callBack/transaction/game/bet", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object bet(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  老虎機下注bet回调params:{},wtoken:{}", JSONObject.toJSONString(jsonObject), wtoken);
        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqBetCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  老虎機下注bet回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 結束回合並統整該回合贏分
     */
    @RequestMapping(value = "/callBack/transaction/game/endround", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object endround(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  結束回合並統整該回合贏分endround回调params:{},wtoken:{}", JSONObject.toJSONString(jsonObject), wtoken);
        CqEndroundCallBackReq endroundDataCallBackReq = JSONObject.toJavaObject(jsonObject,CqEndroundCallBackReq.class);
        Object object = cqCallbackService.endround(endroundDataCallBackReq, ip, wtoken);
        logger.info("cqCallBack  結束回合並統整該回合贏分endround回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 活動派彩
     */
    @RequestMapping(value = "/callBack/transaction/user/payoff", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object payOff(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  payOff回调,params:{},wtoken:{}", JSONObject.toJSONString(jsonObject), wtoken);
        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqPayOffCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  payOff回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 遊戲紅利
     */
    @RequestMapping(value = "/callBack/transaction/game/bonus", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object bonus(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  bonus回调,params:{},wtoken:{}", JSONObject.toJSONString(jsonObject), wtoken);
        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqBonusCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  bonus回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 針對完成的訂單做補款
     */
    @RequestMapping(value = "/callBack/transaction/game/credit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object credit(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  credit回调,params:{},wtoken:{}", JSONObject.toJSONString(jsonObject), wtoken);
        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqCreditCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  credit回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 針對完成的訂單做扣款
     */
    @RequestMapping(value = "/callBack/transaction/game/debit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object debit(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  debit回调,params:{},wtoken:{}", JSONObject.toJSONString(jsonObject), wtoken);
        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqDebitCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  debit回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 遊戲结算
     */
    @RequestMapping(value = "/callBack/transaction/game/rollin", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object rollin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  rollin回调,params:{},wtoken:{}", JSONObject.toJSONString(jsonObject), wtoken);
        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqRollinCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  rollin回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 查詢交易紀錄
     */
    @RequestMapping(value = "/callBack/transaction/record/{mtcode}", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object record(@PathVariable("mtcode")String mtcode,HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  record,mtcode:{},wtoken:{}", mtcode, wtoken);
        Object object = cqCallbackService.cqRecordCallback(mtcode, ip, wtoken);
        logger.info("cqCallBack  record回调返回数据 params:{}", object);
        return object;
    }

}
