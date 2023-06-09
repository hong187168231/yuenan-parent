package com.indo.game.controller.saba;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.saba.*;
import com.indo.game.service.saba.SabaCallbackService;
import com.indo.game.service.saba.impl.SabaCallbackServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/saba/callBack")
public class SabaCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SabaCallbackService sabaCallbackService;

    protected String getRequestBody(HttpServletRequest request){
        try {
            ServletInputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
    public static void main(String[] args) {
        JSONObject jsonObject = JSONObject.parseObject("{\"key\":\"y270zsfdqy\",\"message\":{\"action\":\"GetBalance\",\"userId\":\"swuserid\"}}");
        System.out.println("json:"+JSONObject.toJSONString(jsonObject));
        SabaCallBackReq<SabaCallBackParentReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        System.out.println("bean:"+sabaCallBackReq);
        SabaCallbackService sabaCallbackService = new SabaCallbackServiceImpl();
        Object getBalance = sabaCallbackService.getBalance(sabaCallBackReq);
    }
    /**
     * 取得用户的余额
     */
    @RequestMapping(value="/getbalance",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(HttpServletRequest request) {
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
//        JSONObject jsonObject = JSONObject.parseObject(SabaGZIPUtil.uncompressToString(SabaGZIPUtil.compress(result)));
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack GetBalance 回调,取得用户的余额 params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<SabaCallBackParentReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        System.out.println("bean:"+sabaCallBackReq);
        Object getBalance = sabaCallbackService.getBalance(sabaCallBackReq);
        logger.info("sabaCallBack GetBalance 回调返回数据,取得用户的余额 params:{}",JSONObject.toJSONString(getBalance));
        return getBalance;

    }

    /**
     * 扣除投注金额 当下注单状态为未结算
     */
    @RequestMapping(value="/placebet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object placeBet( HttpServletRequest request) {
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack placeBet 回调,扣除投注金额 params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<SabaCallBackPlaceBetReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object placeBet = sabaCallbackService.placeBet(sabaCallBackReq);
        logger.info("sabaCallBack placeBet 回调返回数据,扣除投注金额 params:{}",JSONObject.toJSONString(placeBet));
        return placeBet;
    }

    /**
     * 当沙巴体育通过 PlaceBet 方法收到成功结果，沙巴体育将会呼叫 ConfirmBet
     */
    @RequestMapping(value="/confirmbet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object confirmBet( HttpServletRequest request) {
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack confirmbet 回调,沙巴体育将会呼叫 ConfirmBet params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<SabaCallBackConfirmBetReq<TicketInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object confirmBet = sabaCallbackService.confirmBet(sabaCallBackReq);
        logger.info("sabaCallBack confirmbet 回调返回数据,沙巴体育将会呼叫 ConfirmBet params:{}",JSONObject.toJSONString(confirmBet));
        return confirmBet;
    }

    /**
     * 取消投注
     */
    @RequestMapping(value="/cancelbet",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object cancelBet( HttpServletRequest request) {
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack cancelbet 回调,回滚 params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<SabaCallBackCancelBetReq<TradingInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object cancelBet = sabaCallbackService.cancelBet(sabaCallBackReq);
        logger.info("sabaCallBack cancelbet 回调返回数据,回滚 params:{}",JSONObject.toJSONString(cancelBet));
        return cancelBet;
    }

    /**
     * 结算
     */
    @RequestMapping(value="/settle",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object settle(HttpServletRequest request) {
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack settle 回调,结算投注 params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object settle = sabaCallbackService.settle(sabaCallBackReq);
        logger.info("sabaCallBack settle 回调返回数据,结算投注 params:{}",JSONObject.toJSONString(settle));
        return settle;
    }

    /**
     * 重新结算
     */
    @RequestMapping(value="/resettle",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object resettle( HttpServletRequest request)  {
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack resettle 回调,重新结算投注 params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object resettle = sabaCallbackService.resettle(sabaCallBackReq);
        logger.info("sabaCallBack resettle 回调返回数据,重新结算投注 params:{}",JSONObject.toJSONString(resettle));
        return resettle;
    }

    /**
     * 撤销结算投注
     */
    @RequestMapping(value="/unsettle",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object unsettle( HttpServletRequest request){
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack unsettle 回调,撤销结算投注 params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object unsettle = sabaCallbackService.unsettle(sabaCallBackReq);
        logger.info("sabaCallBack unsettle 回调返回数据,撤销结算投注 params:{}",JSONObject.toJSONString(unsettle));
        return unsettle;
    }

    /**
     * 沙巴体育通过此方法提供下注细节给厂商
     */
    @RequestMapping(value="/placeBetParlay",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object placeBetParlay( HttpServletRequest request){
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack placeBetParlay 回调,下注细节 params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<SabaCallBackPlaceBetParlayReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object placeBetParlay = sabaCallbackService.placeBetParlay(sabaCallBackReq);
        logger.info("sabaCallBack placeBetParlay 回调返回数据,下注细节 params:{}",JSONObject.toJSONString(placeBetParlay));
        return placeBetParlay;
    }

    /**
     * 当沙巴体育通过 PlaceBetParlay 方法收到成功结果，沙巴体育将会呼叫 ConfirmBetParlay
     */
    @RequestMapping(value="/confirmBetParlay",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object confirmBetParlay( HttpServletRequest request){
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack confirmBetParlay 回调,当沙巴体育通过 PlaceBetParlay 方法收到成功结果 params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<SabaCallBackConfirmBetParlayReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object confirmBetParlay = sabaCallbackService.confirmBetParlay(sabaCallBackReq);
        logger.info("sabaCallBack confirmBetParlay 回调返回数据,当沙巴体育通过 PlaceBetParlay 方法收到成功结果 params:{}",JSONObject.toJSONString(confirmBetParlay));
        return confirmBetParlay;
    }

    /**
     * 此方法支持推广活动或任何会影响玩家钱包余额
     * 当呼叫失败时，将会持续呼叫 Adjust Balance 直到成功或达到重试最大次数上限。
     */
    @RequestMapping(value="/adjustbalance",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object adjustbalance( HttpServletRequest request){
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack adjustbalance 回调,当沙巴体育通过 PlaceBetParlay 方法收到成功结果 params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<SabaCallBackAdjustbalanceReq<SabaCallBackAdjustbalanceInfoReq>> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object adjustbalance = sabaCallbackService.adjustbalance(sabaCallBackReq);
        logger.info("sabaCallBack adjustbalance 回调返回数据,当沙巴体育通过 PlaceBetParlay 方法收到成功结果 params:{}",JSONObject.toJSONString(adjustbalance));
        return adjustbalance;
    }

    /**
     * 厂商提供此方法，沙巴体育通过呼叫此方法提供下注细节给厂商
     *  本方法支持快乐彩、彩票、桌面游戏产品
     */
    @RequestMapping(value="/placeBet3rd",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object placeBet3rd( HttpServletRequest request){
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack placeBet3rd 回调,本方法支持快乐彩、彩票、桌面游戏产品 params:{},ip:{}",gzipStr,ip);
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
    public Object confirmBet3rd( HttpServletRequest request){
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack confirmBet3rd 回调,本方法支持快乐彩、彩票、桌面游戏产品 params:{},ip:{}",gzipStr,ip);
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
    public Object cashOut( HttpServletRequest request){
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack cashOut 回调,当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易 params:{},ip:{}",gzipStr,ip);
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
    public Object updateBet( HttpServletRequest request){
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack updateBet 回调,因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。 params:{},ip:{}",gzipStr,ip);
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
    public Object healthcheck( HttpServletRequest request){
        String gzipStr = this.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(gzipStr);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("sabaCallBack healthcheck 回调,检查沙巴体育与厂商之间的连结是否有效。 params:{},ip:{}",gzipStr,ip);
        SabaCallBackReq<HealthCheckReq> sabaCallBackReq = JSONObject.toJavaObject(jsonObject,SabaCallBackReq.class);
        Object healthcheck = sabaCallbackService.healthcheck(sabaCallBackReq);
        logger.info("sabaCallBack healthcheck 回调返回数据,检查沙巴体育与厂商之间的连结是否有效。 params:{}",healthcheck);
        return healthcheck;
    }


}
