package com.indo.game.controller.saba;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.SabaGZIPUtil;
import com.indo.game.pojo.dto.saba.*;
import com.indo.game.service.saba.SabaCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.jboss.resteasy.annotations.GZIP;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/saba/callBack")
public class SabaCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SabaCallbackService sabaCallbackService;

    /**
     * 取得用户的余额
     */
    @RequestMapping(value="/getbalance",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(@GZIP @RequestBody String result,HttpServletRequest request) {
        logger.info("sabaCallBack GetBalance 回调,取得用户的余额 result:{}",result);
        JSONObject jsonObject = SabaGZIPUtil.getJSONObject(request);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack GetBalance 回调,取得用户的余额 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackParentReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object getBalance = sabaCallbackService.getBalance(sabaCallBackReq);
        logger.info("sabaCallBack GetBalance 回调返回数据,取得用户的余额 params:{}",getBalance);
        return getBalance;

    }

    /**
     * 扣除投注金额 当下注单状态为未结算
     */
    @RequestMapping(value="/placebet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object placeBet(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack placeBet 回调,扣除投注金额 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackPlaceBetReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object placeBet = sabaCallbackService.placeBet(sabaCallBackReq);
        logger.info("sabaCallBack placeBet 回调返回数据,扣除投注金额 params:{}",placeBet);
        return placeBet;
    }

    /**
     * 当沙巴体育通过 PlaceBet 方法收到成功结果，沙巴体育将会呼叫 ConfirmBet
     */
    @RequestMapping(value="/confirmbet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object confirmBet(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack Deduct 回调,沙巴体育将会呼叫 ConfirmBet params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackConfirmBetReq<TicketInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object confirmBet = sabaCallbackService.confirmBet(sabaCallBackReq);
        logger.info("sabaCallBack Deduct 回调返回数据,沙巴体育将会呼叫 ConfirmBet params:{}",confirmBet);
        return confirmBet;
    }

    /**
     * 取消投注
     */
    @RequestMapping(value="/cancelBet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object cancelBet(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack rollback 回调,回滚 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackCancelBetReq<TradingInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object cancelBet = sabaCallbackService.cancelBet(sabaCallBackReq);
        logger.info("sabaCallBack rollback 回调返回数据,回滚 params:{}",cancelBet);
        return cancelBet;
    }

    /**
     * 结算
     */
    @RequestMapping(value="/settle",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object settle(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack settle 回调,结算投注 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object settle = sabaCallbackService.settle(sabaCallBackReq);
        logger.info("sabaCallBack settle 回调返回数据,结算投注 params:{}",settle);
        return settle;
    }

    /**
     * 重新结算
     */
    @RequestMapping(value="/resettle",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object resettle(@RequestBody JSONObject jsonObject, HttpServletRequest request)  {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack resettle 回调,重新结算投注 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object resettle = sabaCallbackService.resettle(sabaCallBackReq);
        logger.info("sabaCallBack resettle 回调返回数据,重新结算投注 params:{}",resettle);
        return resettle;
    }

    /**
     * 撤销结算投注
     */
    @RequestMapping(value="/unsettle",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object unsettle(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack unsettle 回调,撤销结算投注 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object unsettle = sabaCallbackService.unsettle(sabaCallBackReq);
        logger.info("sabaCallBack unsettle 回调返回数据,撤销结算投注 params:{}",unsettle);
        return unsettle;
    }

    /**
     * 沙巴体育通过此方法提供下注细节给厂商
     */
    @RequestMapping(value="/placeBetParlay",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object placeBetParlay(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack placeBetParlay 回调,下注细节 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackPlaceBetParlayReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object placeBetParlay = sabaCallbackService.placeBetParlay(sabaCallBackReq);
        logger.info("sabaCallBack placeBetParlay 回调返回数据,下注细节 params:{}",placeBetParlay);
        return placeBetParlay;
    }

    /**
     * 当沙巴体育通过 PlaceBetParlay 方法收到成功结果，沙巴体育将会呼叫 ConfirmBetParlay
     */
    @RequestMapping(value="/confirmBetParlay",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object confirmBetParlay(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack confirmBetParlay 回调,当沙巴体育通过 PlaceBetParlay 方法收到成功结果 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackConfirmBetParlayReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object confirmBetParlay = sabaCallbackService.confirmBetParlay(sabaCallBackReq);
        logger.info("sabaCallBack confirmBetParlay 回调返回数据,当沙巴体育通过 PlaceBetParlay 方法收到成功结果 params:{}",confirmBetParlay);
        return confirmBetParlay;
    }

    /**
     * 此方法支持推广活动或任何会影响玩家钱包余额
     * 当呼叫失败时，将会持续呼叫 Adjust Balance 直到成功或达到重试最大次数上限。
     */
    @RequestMapping(value="/adjustbalance",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object adjustbalance(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack confirmBetParlay 回调,当沙巴体育通过 PlaceBetParlay 方法收到成功结果 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackAdjustbalanceReq<SabaCallBackAdjustbalanceInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object adjustbalance = sabaCallbackService.adjustbalance(sabaCallBackReq);
        logger.info("sabaCallBack confirmBetParlay 回调返回数据,当沙巴体育通过 PlaceBetParlay 方法收到成功结果 params:{}",adjustbalance);
        return adjustbalance;
    }

    /**
     * 厂商提供此方法，沙巴体育通过呼叫此方法提供下注细节给厂商
     *  本方法支持快乐彩、彩票、桌面游戏产品
     */
    @RequestMapping(value="/placeBet3rd",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object placeBet3rd(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack placeBet3rd 回调,本方法支持快乐彩、彩票、桌面游戏产品 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackPlaceBet3rdReq<PlaceBet3rdTicketInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object placeBet3rd = sabaCallbackService.placeBet3rd(sabaCallBackReq);
        logger.info("sabaCallBack placeBet3rd 回调返回数据,本方法支持快乐彩、彩票、桌面游戏产品 params:{}",placeBet3rd);
        return placeBet3rd;
    }

    /**
     * 厂商提供此方法，沙巴体育通过呼叫此方法提供下注细节给厂商
     *  本方法支持快乐彩、彩票、桌面游戏产品
     */
    @RequestMapping(value="/confirmBet3rd",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object confirmBet3rd(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack confirmBet3rd 回调,本方法支持快乐彩、彩票、桌面游戏产品 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackConfirmBet3rdReq<ConfirmBet3rdTicketInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object confirmBet3rd = sabaCallbackService.confirmBet3rd(sabaCallBackReq);
        logger.info("sabaCallBack confirmBet3rd 回调返回数据,本方法支持快乐彩、彩票、桌面游戏产品 params:{}",confirmBet3rd);
        return confirmBet3rd;
    }

    /**
     * 当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易
     */
    @RequestMapping(value="/cashOut",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object cashOut(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack cashOut 回调,当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackCashOutReq<CashOutTicketInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object cashOut = sabaCallbackService.cashOut(sabaCallBackReq);
        logger.info("sabaCallBack cashOut 回调返回数据,当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易 params:{}",cashOut);
        return cashOut;
    }

    /**
     * 因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。
     */
    @RequestMapping(value="/updateBet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object updateBet(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack updateBet 回调,因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<SabaCallBackCashOutReq<UpdateBetTicketInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object updateBet = sabaCallbackService.updateBet(sabaCallBackReq);
        logger.info("sabaCallBack updateBet 回调返回数据,因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。 params:{}",updateBet);
        return updateBet;
    }

    /**
     * 检查沙巴体育与厂商之间的连结是否有效。
     */
    @RequestMapping(value="/healthcheck",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object healthcheck(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack healthcheck 回调,检查沙巴体育与厂商之间的连结是否有效。 params:{},ip:{}",JSONObject.toJSONString(jsonObject),ip);
        SabaCallBackReq<HealthCheckReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object healthcheck = sabaCallbackService.healthcheck(sabaCallBackReq);
        logger.info("sabaCallBack healthcheck 回调返回数据,检查沙巴体育与厂商之间的连结是否有效。 params:{}",healthcheck);
        return healthcheck;
    }


}
