package com.indo.game.controller.tcgwin;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.vo.callback.tcgwin.TCGWinDebitCallBackReq;
import com.indo.game.pojo.vo.callback.tcgwin.TCGWinGetBalanceCallBackReq;
import com.indo.game.pojo.vo.callback.tcgwin.TransactionData;
import com.indo.game.service.tcg.TCGWinCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/TCG/callBack")
public class TcgwinCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TCGWinCallbackService tcgWinCallbackService;


    @RequestMapping(value = "/GetBalance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("TcgwinCallBack  取得玩家身上金额getBalance回调params:{}", jsonObject);
        TCGWinGetBalanceCallBackReq tcgWinGetBalanceCallBackReq = JSONObject.toJavaObject(jsonObject,TCGWinGetBalanceCallBackReq.class);
        Object object = tcgWinCallbackService.getUserBalance(tcgWinGetBalanceCallBackReq, ip);
        logger.info("TcgwinCallBack  取得玩家身上金额getBalance回调返回数据 params:{}", object);
        return object;
    }

    /**
     * Debit API扣除玩家帐户金额接口
     */
    @RequestMapping(value = "/Debit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object debit(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("TcgwinCallBack  Debit API扣除玩家帐户金额接口回调params:{}", jsonObject);
        TCGWinDebitCallBackReq<TransactionData> tcgWinDebitCallBackReq = JSONObject.toJavaObject(jsonObject,TCGWinDebitCallBackReq.class);
        Object object = tcgWinCallbackService.debit(tcgWinDebitCallBackReq, ip);
        logger.info("TcgwinCallBack  Debit API扣除玩家帐户金额接口回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/Credit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object credit(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("TcgwinCallBack  Credit API 增加玩家帐户金额接口回调params:{}", jsonObject);
        TCGWinDebitCallBackReq<TransactionData> tcgWinDebitCallBackReq = JSONObject.toJavaObject(jsonObject,TCGWinDebitCallBackReq.class);
        Object object = tcgWinCallbackService.credit(tcgWinDebitCallBackReq, ip);
        logger.info("TcgwinCallBack  Credit API 增加玩家帐户金额接口回调返回数据 params:{}", object);
        return object;
    }


}
