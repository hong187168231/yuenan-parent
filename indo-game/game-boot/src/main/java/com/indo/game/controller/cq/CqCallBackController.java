package com.indo.game.controller.cq;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.cq.*;
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
     * 老虎機下注
     */
    @RequestMapping(value = "/callBack/transaction/game/bet", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object bet(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  老虎機下注bet回调params:{},wtoken:{}", JSONObject.toJSONString(cqApiRequestData), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
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
    public Object endround(CqEndroundCallBackReq endroundDataCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  結束回合並統整該回合贏分endround回调params:{},wtoken:{}", JSONObject.toJSONString(endroundDataCallBackReq), wtoken);
//        CqEndroundCallBackReq endroundDataCallBackReq = JSONObject.toJavaObject(jsonObject,CqEndroundCallBackReq.class);
        Object object = cqCallbackService.endround(endroundDataCallBackReq, ip, wtoken);
        logger.info("cqCallBack  結束回合並統整該回合贏分endround回调返回数据 params:{}", object);
        return object;
    }
    /**
     * 此API是為牌桌及漁機遊戲，轉出一定額度金額至牌桌或漁機遊戲而調用
     */
    @RequestMapping(value = "/callBack/transaction/game/rollout", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object rollout(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  rollout此API是為牌桌及漁機遊戲，轉出一定額度金額至牌桌或漁機遊戲而調用回调params:{},wtoken:{}", JSONObject.toJSONString(cqApiRequestData), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqRolloutCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  rollout此API是為牌桌及漁機遊戲，轉出一定額度金額至牌桌或漁機遊戲而調用回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 把玩家所有的錢領出，轉入漁機遊戲
     */
    @RequestMapping(value = "/callBack/transaction/game/takeall", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object takeall(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  takeall把玩家所有的錢領出，轉入漁機遊戲回调params:{},wtoken:{}", JSONObject.toJSONString(cqApiRequestData), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqTakeallCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  takeall把玩家所有的錢領出，轉入漁機遊戲回调返回数据 params:{}", object);
        return object;
    }
    /**
     * 遊戲结算
     */
    @RequestMapping(value = "/callBack/transaction/game/rollin", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object rollin(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  rollin回调,params:{},wtoken:{}", JSONObject.toJSONString(cqApiRequestData), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqRollinCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  rollin回调返回数据 params:{}", object);
        return object;
    }
    /**
     * 針對完成的訂單做扣款
     */
    @RequestMapping(value = "/callBack/transaction/game/debit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object debit(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  debit回调,params:{},wtoken:{}", JSONObject.toJSONString(cqApiRequestData), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqDebitCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  debit回调返回数据 params:{}", object);
        return object;
    }
    /**
     * 針對完成的訂單做補款
     */
    @RequestMapping(value = "/callBack/transaction/game/credit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object credit(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  credit回调,params:{},wtoken:{}", JSONObject.toJSONString(cqApiRequestData), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqCreditCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  credit回调返回数据 params:{}", object);
        return object;
    }
    /**
     * 遊戲紅利
     */
    @RequestMapping(value = "/callBack/transaction/game/bonus", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object bonus(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  bonus回调,params:{},wtoken:{}", JSONObject.toJSONString(cqApiRequestData), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqBonusCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  bonus回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 活動派彩
     */
    @RequestMapping(value = "/callBack/transaction/user/payoff", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object payOff(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  payOff回调,params:{},wtoken:{}", JSONObject.toJSONString(cqApiRequestData), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqPayOffCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  payOff回调返回数据 params:{}", object);
        return object;
    }
    /**
     * 退款 bet/rollout/takeall 的金額
     */
    @RequestMapping(value = "/callBack/transaction/game/refund", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object refund(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  refund退款 bet/rollout/takeall 的金額回调,params:{},wtoken:{}", JSONObject.toJSONString(cqApiRequestData), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqRefundCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack  refund退款 bet/rollout/takeall 的金額回调返回数据 params:{}", object);
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
     * 專用於體彩批次下注
     */
    @RequestMapping(value = "/callBack/transaction/game/bets", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object bets(CqSportsCallBackReq<CqSportsInfoCallBackReq> cqSportsCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  專用於體彩批次下注bets回调params:{},wtoken:{}", JSONObject.toJSONString(cqSportsCallBackReq), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqBetsCallback(cqSportsCallBackReq, ip, wtoken);
        logger.info("cqCallBack  專用於體彩批次下注bets回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 專用於體彩批次押注退還
     */
    @RequestMapping(value = "/callBack/transaction/game/refund", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object refunds(CqSportsRefudsCallBackReq cqSportsRefudsCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  refunds專用於體彩批次押注退還回调,params:{},wtoken:{}", JSONObject.toJSONString(cqSportsRefudsCallBackReq), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqRefundsCallback(cqSportsRefudsCallBackReq, ip, wtoken);
        logger.info("cqCallBack  refunds專用於體彩批次押注退還回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 專用於體彩批次押注退還
     */
    @RequestMapping(value = "/callBack/transaction/game/cancel", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object cancel(CqSportsRefudsCallBackReq cqSportsRefudsCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  cancel專用於體彩批次押注退還回调,params:{},wtoken:{}", JSONObject.toJSONString(cqSportsRefudsCallBackReq), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqCancelCallback(cqSportsRefudsCallBackReq, ip, wtoken);
        logger.info("cqCallBack  cancel專用於體彩批次押注退還回调返回数据 params:{}", object);
        return object;
    }
    /**
     * 專用於體彩修改派果
     */
    @RequestMapping(value = "/callBack/transaction/game/amend", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object amend(CqSportsAmendCallBackReq<CqSportsAmendDataCallBackReq> cqSportsAmendCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  amend專用於體彩修改派果回调,params:{},wtoken:{}", JSONObject.toJSONString(cqSportsAmendCallBackReq), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqAmendCallback(cqSportsAmendCallBackReq, ip, wtoken);
        logger.info("cqCallBack  amend專用於體彩修改派果回调返回数据 params:{}", object);
        return object;
    }
    /**
     * 專用於體彩多人多派彩
     */
    @RequestMapping(value = "/callBack/transaction/game/wins", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object wins(CqSportsWinsCallBackReq<CqSportsEventCallBackReq> callBackReqCqSportsWinsCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  wins專用於體彩批次押注退還回调,params:{},wtoken:{}", JSONObject.toJSONString(callBackReqCqSportsWinsCallBackReq), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqWinsCallback(callBackReqCqSportsWinsCallBackReq, ip, wtoken);
        logger.info("cqCallBack  wins專用於體彩批次押注退還回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 專用於體彩批次修改派果
     */
    @RequestMapping(value = "/callBack/transaction/game/amends", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object amends(CqSportsAmendCallBackReq<CqSportsAmendDataCallBackReq> cqSportsAmendCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack  amends專用於體彩修批次修改派果回调,params:{},wtoken:{}", JSONObject.toJSONString(cqSportsAmendCallBackReq), wtoken);
//        CqBetCallBackReq cqApiRequestData = JSONObject.toJavaObject(jsonObject,CqBetCallBackReq.class);
        Object object = cqCallbackService.cqAmendsCallback(cqSportsAmendCallBackReq, ip, wtoken);
        logger.info("cqCallBack  amends專用於體彩批次修改派果回调返回数据 params:{}", object);
        return object;
    }
}
