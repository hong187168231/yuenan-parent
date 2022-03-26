package com.indo.game.controller.fc;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.FCHashAESDecrypt;
import com.indo.game.common.util.T9AESDecrypt;
import com.indo.game.pojo.dto.fc.FCBalanceCallbackReq;
import com.indo.game.pojo.dto.fc.FCBetCallbackReq;
import com.indo.game.pojo.dto.fc.FCCancelCallbackReq;
import com.indo.game.service.fc.FCCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * FC 回调服务
 */
@RestController
@RequestMapping("/fc/callBack")
public class FCCallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FCCallbackService fcCallbackService;


    /**
     * 查询玩家余额
     */
    @RequestMapping(value = "/balance", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object balance(String Params, HttpServletRequest request) {
        String jsonStr = null;
        try {
            jsonStr = FCHashAESDecrypt.decrypt(Params, OpenAPIProperties.FC_AGENT_KEY);
        } catch (Exception e) {
            logger.info("FCCallback callBack balance 解密失败 {}, {}", Params, e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("FCCallback balance 回调,IP:" + ip + " params:{}", jsonStr);
        Object object = fcCallbackService.balance(JSONObject.parseObject(jsonStr, FCBalanceCallbackReq.class), ip);
        logger.info("FCCallback balance 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * FC 游戏下注
     */
    @RequestMapping(value = "/bet", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object bet(String Params, HttpServletRequest request) {
        String jsonStr = null;
        try {
            jsonStr = FCHashAESDecrypt.decrypt(Params, OpenAPIProperties.FC_AGENT_KEY);
        } catch (Exception e) {
            logger.info("FCCallback callBack bet 解密失败 {}, {}", Params, e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("FCCallback bet 回调,IP:" + ip + " params:{}", jsonStr);
        Object object = fcCallbackService.bet(JSONObject.parseObject(jsonStr, FCBetCallbackReq.class), ip);
        logger.info("FCCallback bet 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 取消交易
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object cancel(String Params, HttpServletRequest request) {
        String jsonStr = null;
        try {
            jsonStr = FCHashAESDecrypt.decrypt(Params, OpenAPIProperties.FC_AGENT_KEY);
        } catch (Exception e) {
            logger.info("FCCallback callBack cancel 解密失败 {}, {}", Params, e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("FCCallback cancel 回调,IP:" + ip + " params:{}", jsonStr);
        Object object = fcCallbackService.cancel(JSONObject.parseObject(jsonStr, FCCancelCallbackReq.class), ip);
        logger.info("FCCallback cancel 回调返回数据, params:{}", object);
        return object;
    }

}
