package com.indo.game.controller.pp;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.pp.PpAuthenticateCallBackReq;
import com.indo.game.pojo.dto.pp.PpBalanceCallBackReq;
import com.indo.game.pojo.dto.pp.PpBetCallBackReq;
import com.indo.game.pojo.dto.pp.PpBonusWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpJackpotWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpPromoWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpRefundWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpResultCallBackReq;
import com.indo.game.service.pp.PpCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/PP/callBack")
public class PpCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PpCallbackService ppCallbackService;


    /**
     * 令牌验证
     */
    @RequestMapping(value = "/Authenticate", method = RequestMethod.POST,produces = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object authenticate( HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppCallBack authenticate 回调,params:{}", JSONObject.toJSONString(request.getParameterMap()));
        PpAuthenticateCallBackReq ppAuthenticateCallBackReq = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(request.getParameterMap())),PpAuthenticateCallBackReq.class);
        Object object = ppCallbackService.authenticate(ppAuthenticateCallBackReq, ip);
        logger.info("ppCallBack authenticate 回调权限验证返回数据 params:{}", object);
        return object;
    }

    /**
     * 查询余额
     */
    @RequestMapping(value = "/Balance", method = RequestMethod.POST,produces = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(PpBalanceCallBackReq ppBalanceCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppCallBack getBalance 回调,params:{}", JSONObject.toJSONString(ppBalanceCallBackReq));
        Object object = ppCallbackService.getBalance(ppBalanceCallBackReq, ip);
        logger.info("ppCallBack getBalance 回调查询余额返回数据 params:{}", object);
        return object;
    }


    /**
     * 下注
     */
    @RequestMapping(value = "/Bet", method = RequestMethod.POST,produces = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object bet(PpBetCallBackReq ppBetCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppCallBack bet 回调,params:{}", JSONObject.toJSONString(ppBetCallBackReq));
        Object object = ppCallbackService.bet(ppBetCallBackReq, ip);
        logger.info("ppCallBack bet 回调下注返回数据 params:{}", object);
        return object;
    }

    /**
     * 将赢奖金额添加到玩家余额中。返回更新余额值。 Result
     */
    @RequestMapping(value = "/Result", method = RequestMethod.POST,produces = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object result(PpResultCallBackReq ppResultCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppCallBack result 回调,params:{}", JSONObject.toJSONString(ppResultCallBackReq));
        Object object = ppCallbackService.result(ppResultCallBackReq, ip);
        logger.info("ppCallBack result 回调玩家中奖返回数据 params:{}", object);
        return object;
    }

    /**
     * 通知娱乐场运营商免费回合已结束以及玩家余额应增加的奖励金额。 BonusWin
     */
    @RequestMapping(value = "/BonusWin", method = RequestMethod.POST,produces = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object bonusWin(PpBonusWinCallBackReq ppBonusWinCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppCallBack bonusWin 回调,params:{}", JSONObject.toJSONString(ppBonusWinCallBackReq));
        Object object = ppCallbackService.bonusWin(ppBonusWinCallBackReq, ip);
        logger.info("ppCallBack bonusWin 回调玩家免费回合中奖返回数据 params:{}", object);
        return object;
    }

    /**
     * 通过这种方法，Pragmatic Play 系统将通知娱乐场运营商有关累积奖金赢奖的信息。 JackpotWin
     */
    @RequestMapping(value = "/JackpotWin", method = RequestMethod.POST,produces = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object jackpotWin(PpJackpotWinCallBackReq ppJackpotWinCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppCallBack refund 回调,params:{}", JSONObject.toJSONString(ppJackpotWinCallBackReq));
        Object object = ppCallbackService.jackpotWin(ppJackpotWinCallBackReq, ip);
        logger.info("ppCallBack refund 回调有关累积奖金赢奖返回数据 params:{}", object);
        return object;
    }

    /**
     * 通知娱乐场运营商锦标赛活动已结束，玩家的现金余额应增加promoWin 中的金额。 PromoWin
     */
    @RequestMapping(value = "/PromoWin", method = RequestMethod.POST,produces = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object promoWin(PpPromoWinCallBackReq ppPromoWinCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppCallBack refund 回调,params:{}", JSONObject.toJSONString(ppPromoWinCallBackReq));
        Object object = ppCallbackService.promoWin(ppPromoWinCallBackReq, ip);
        logger.info("ppCallBack refund 回调退款返回数据 params:{}", object);
        return object;
    }

    /**
     * 退款到玩家余额。此方法用于在游戏无法结束时取消赌注。 Refund
     */
    @RequestMapping(value = "/Refund", method = RequestMethod.POST,produces = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object refund(PpRefundWinCallBackReq ppRefundWinCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppCallBack refund 回调,params:{}", JSONObject.toJSONString(ppRefundWinCallBackReq));
        Object object = ppCallbackService.refund(ppRefundWinCallBackReq, ip);
        logger.info("ppCallBack refund 回调退款返回数据 params:{}", object);
        return object;
    }

}
