package com.indo.game.controller.saba;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.game.pojo.dto.saba.*;
import com.indo.game.service.saba.SabaCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saba")
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
    public Object getBalance(SabaCallBackReq<SabaCallBackParentReq> sabaCallBackReqq) {
        logger.info("sabaCallBack {} GetBalance 回调,取得用户的余额 params:{}",JSONObject.toJSONString(sabaCallBackReqq));
        Object getBalance = sabaCallbackService.getBalance(sabaCallBackReqq);
        logger.info("sabaCallBack {} GetBalance 回调返回数据,取得用户的余额 params:{}",getBalance);
        return getBalance;
    }

    /**
     * 扣除投注金额 当下注单状态为未结算
     */
    @RequestMapping(value="/placebet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object placeBet(SabaCallBackReq<SabaCallBackPlaceBetReq> sabaCallBackReq) {
        logger.info("sabaCallBack {} placeBet 回调,扣除投注金额 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object placeBet = sabaCallbackService.placeBet(sabaCallBackReq);
        logger.info("sabaCallBack {} placeBet 回调返回数据,扣除投注金额 params:{}",placeBet);
        return placeBet;
    }

    /**
     * 当沙巴体育通过 PlaceBet 方法收到成功结果，沙巴体育将会呼叫 ConfirmBet
     */
    @RequestMapping(value="/confirmbet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object confirmBet(SabaCallBackReq<SabaCallBackConfirmBetReq<TicketInfoReq>> sabaCallBackReq) {
        logger.info("sabaCallBack {} Deduct 回调,沙巴体育将会呼叫 ConfirmBet params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object confirmBet = sabaCallbackService.confirmBet(sabaCallBackReq);
        logger.info("sabaCallBack {} Deduct 回调返回数据,沙巴体育将会呼叫 ConfirmBet params:{}",confirmBet);
        return confirmBet;
    }

    /**
     * 取消投注
     */
    @RequestMapping(value="/CancelBet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object cancelBet(SabaCallBackReq<SabaCallBackCancelBetReq<TradingInfoReq>> sabaCallBackReq) {
        logger.info("sabaCallBack {} rollback 回调,回滚 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object cancelBet = sabaCallbackService.cancelBet(sabaCallBackReq);
        logger.info("sabaCallBack {} rollback 回调返回数据,回滚 params:{}",cancelBet);
        return cancelBet;
    }

    /**
     * 结算
     */
    @RequestMapping(value="/Settle",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object settle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq) {
        logger.info("sabaCallBack {} settle 回调,结算投注 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object settle = sabaCallbackService.settle(sabaCallBackReq);
        logger.info("sabaCallBack {} settle 回调返回数据,结算投注 params:{}",settle);
        return settle;
    }

    /**
     * 重新结算
     */
    @RequestMapping(value="/resettle",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object resettle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq)  {
        logger.info("sabaCallBack {} resettle 回调,重新结算投注 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object resettle = sabaCallbackService.resettle(sabaCallBackReq);
        logger.info("sabaCallBack {} resettle 回调返回数据,重新结算投注 params:{}",resettle);
        return resettle;
    }

    /**
     * 撤销结算投注
     */
    @RequestMapping(value="/unsettle",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object unsettle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq){
        logger.info("sabaCallBack {} unsettle 回调,撤销结算投注 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object unsettle = sabaCallbackService.unsettle(sabaCallBackReq);
        logger.info("sabaCallBack {} unsettle 回调返回数据,撤销结算投注 params:{}",unsettle);
        return unsettle;
    }

    /**
     * 沙巴体育通过此方法提供下注细节给厂商
     */
    @RequestMapping(value="/placeBetParlay",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object placeBetParlay(SabaCallBackReq<SabaCallBackPlaceBetParlayReq> sabaCallBackReq){
        logger.info("sabaCallBack {} placeBetParlay 回调,下注细节 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object placeBetParlay = sabaCallbackService.placeBetParlay(sabaCallBackReq);
        logger.info("sabaCallBack {} placeBetParlay 回调返回数据,下注细节 params:{}",placeBetParlay);
        return placeBetParlay;
    }

    /**
     * 当沙巴体育通过 PlaceBetParlay 方法收到成功结果，沙巴体育将会呼叫 ConfirmBetParlay
     */
    @RequestMapping(value="/confirmBetParlay",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object confirmBetParlay(SabaCallBackReq<SabaCallBackConfirmBetParlayReq> sabaCallBackReq){
        logger.info("sabaCallBack {} confirmBetParlay 回调,当沙巴体育通过 PlaceBetParlay 方法收到成功结果 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object confirmBetParlay = sabaCallbackService.confirmBetParlay(sabaCallBackReq);
        logger.info("sabaCallBack {} confirmBetParlay 回调返回数据,当沙巴体育通过 PlaceBetParlay 方法收到成功结果 params:{}",confirmBetParlay);
        return confirmBetParlay;
    }

    /**
     * 厂商提供此方法，沙巴体育通过呼叫此方法提供下注细节给厂商
     *  本方法支持快乐彩、彩票、桌面游戏产品
     */
    @RequestMapping(value="/placeBet3rd",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object placeBet3rd(SabaCallBackReq<SabaCallBackPlaceBet3rdReq<PlaceBet3rdTicketInfoReq>> sabaCallBackReq){
        logger.info("sabaCallBack {} placeBet3rd 回调,本方法支持快乐彩、彩票、桌面游戏产品 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object placeBet3rd = sabaCallbackService.placeBet3rd(sabaCallBackReq);
        logger.info("sabaCallBack {} placeBet3rd 回调返回数据,本方法支持快乐彩、彩票、桌面游戏产品 params:{}",placeBet3rd);
        return placeBet3rd;
    }

    /**
     * 厂商提供此方法，沙巴体育通过呼叫此方法提供下注细节给厂商
     *  本方法支持快乐彩、彩票、桌面游戏产品
     */
    @RequestMapping(value="/confirmBet3rd",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object confirmBet3rd(SabaCallBackReq<SabaCallBackConfirmBet3rdReq<ConfirmBet3rdTicketInfoReq>> sabaCallBackReq){
        logger.info("sabaCallBack {} confirmBet3rd 回调,本方法支持快乐彩、彩票、桌面游戏产品 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object confirmBet3rd = sabaCallbackService.confirmBet3rd(sabaCallBackReq);
        logger.info("sabaCallBack {} confirmBet3rd 回调返回数据,本方法支持快乐彩、彩票、桌面游戏产品 params:{}",confirmBet3rd);
        return confirmBet3rd;
    }

    /**
     * 当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易
     */
    @RequestMapping(value="/cashOut",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object cashOut(SabaCallBackReq<SabaCallBackCashOutReq<CashOutTicketInfoReq>> sabaCallBackReq){
        logger.info("sabaCallBack {} cashOut 回调,当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object cashOut = sabaCallbackService.cashOut(sabaCallBackReq);
        logger.info("sabaCallBack {} cashOut 回调返回数据,当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易 params:{}",cashOut);
        return cashOut;
    }

    /**
     * 因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。
     */
    @RequestMapping(value="/updateBet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object updateBet(SabaCallBackReq<SabaCallBackCashOutReq<UpdateBetTicketInfoReq>> sabaCallBackReq){
        logger.info("sabaCallBack {} updateBet 回调,因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object updateBet = sabaCallbackService.updateBet(sabaCallBackReq);
        logger.info("sabaCallBack {} updateBet 回调返回数据,因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。 params:{}",updateBet);
        return updateBet;
    }

    /**
     * 检查沙巴体育与厂商之间的连结是否有效。
     */
    @RequestMapping(value="/healthcheck",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object healthcheck(SabaCallBackReq<HealthCheckReq> sabaCallBackReq){
        logger.info("sabaCallBack {} healthcheck 回调,检查沙巴体育与厂商之间的连结是否有效。 params:{}",JSONObject.toJSONString(sabaCallBackReq));
        Object healthcheck = sabaCallbackService.healthcheck(sabaCallBackReq);
        logger.info("sabaCallBack {} healthcheck 回调返回数据,检查沙巴体育与厂商之间的连结是否有效。 params:{}",healthcheck);
        return healthcheck;
    }


}
