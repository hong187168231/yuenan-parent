package com.indo.game.controller.yl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.ps.PsCallBackParentReq;
import com.indo.game.pojo.dto.yl.YlCallBackReq;
import com.indo.game.service.ps.PsCallbackService;
import com.indo.game.service.yl.YlCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/YL")
public class YlCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private YlCallbackService ylCallbackService;

    /**
     * 回调
     */
    @RequestMapping(value = "/callBack", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object callBack(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ylCallBack callBack回调,params:{},IP:{}", JSONObject.toJSONString(jsonObject),ip);
        JSONObject jsonsubObject = JSONObject.parseObject(jsonObject.getString("message"));
        Object object = new Object();
        if("getBalance".equals(jsonsubObject.getString("action"))){
            object = this.getBalance(jsonsubObject);
        }else if("settleFishBet".equals(jsonsubObject.getString("action"))){
            object = this.bet(jsonsubObject);
        }else if("voidFishBet".equals(jsonsubObject.getString("action"))){
            object = this.voidFishBet(jsonsubObject);
        }
        return object;
    }

    /**
     * 下注及结算
     */
    public Object bet(JSONObject jsonObject) {

        Object object = ylCallbackService.psBetCallback(jsonObject);
        logger.info("ylCallBack ylSettleFishBet回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 返还押注
     */
    public Object voidFishBet(JSONObject jsonObject) {

        Object object = ylCallbackService.ylVoidFishBetCallback(jsonObject);
        logger.info("ylCallBack voidFishBet回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 获取余额
     */
    public Object getBalance(JSONObject jsonObject) {

        Object object = ylCallbackService.ylGetBalanceCallback(jsonObject);
        logger.info("ylCallBack ylGetBalance回调返回数据 params:{}", object);
        return object;
    }
}
