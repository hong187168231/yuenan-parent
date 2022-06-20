package com.indo.game.controller.v8;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.service.v8.V8CallbackService;
import com.indo.game.service.v8.V8Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping("/v8")
public class V8CallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private V8CallbackService v8CallbackService;

    @Autowired
    private V8Service v8Service;

    @RequestMapping(value = "/crebit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object crebit(@LoginUser LoginInfo loginUser,
                         @RequestBody JSONObject params, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        String platform = params.getString("platform");
        BigDecimal money = params.getBigDecimal("money");
        return v8Service.crebit(loginUser, platform, money, ip);
    }

    @RequestMapping(value = "/balance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object balance4V8(@LoginUser LoginInfo loginUser,
                         @RequestBody JSONObject params, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        String platform = params.getString("platform");
        return v8Service.balance(loginUser, platform, ip);
    }

    @RequestMapping(value = "/callBack", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object callBack(HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        String agent = request.getParameter("agent");
        String timestamp = request.getParameter("timestamp");
        String param = request.getParameter("param");
        String key = request.getParameter("key");
        logger.info("V8Callback callBack 回调, params:{}, {}, {}, {}", agent, timestamp, param, key);
        int method = 0;
        String account = null;
        BigDecimal money = BigDecimal.ZERO;
        try {
            param = GameUtil.decrypt(param, OpenAPIProperties.V8_DESKEY);
            String[] params = param.split("&");
            for (String temp : params) {
                if (temp.indexOf("s=") == 0) {
                    method = Integer.valueOf(temp.replace("s=", ""));
                } else if (temp.indexOf("account=") == 0) {
                    account = temp.replace("account=", "");
                } else if (temp.indexOf("money=") == 0) {
                    money = BigDecimal.valueOf(Double.valueOf(temp.replace("money=", "")));
                }
            }
            // 查询余额
            if (12 == method) {
                return getBalance(agent, timestamp, account, key, ip);
            } else if (13 == method) {
                // 上分
                return debit(agent, timestamp, account, key, money, ip);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 22);
            return jsonObject;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 8);
        return jsonObject;
    }

    // 查余额
    private Object getBalance(String agent, String timestamp, String account, String key, String ip) {
        logger.info("V8Callback getBalance 回调, params:{}, {}, {}, {}", agent, timestamp, account, key);
        Object object = v8CallbackService.getBalance(agent, timestamp, account, key, ip);
        logger.info("V8Callback getBalance 回调返回数据 params:{}", object);
        return object;
    }

    // 上分
    private Object debit(String agent, String timestamp, String account, String key, BigDecimal money, String ip) {
        logger.info("V8Callback debit 回调, params:{}, {}, {}, {}, {}", agent, timestamp, account, key, money);
        Object object = v8CallbackService.debit(agent, timestamp, account, key, money, ip);
        logger.info("V8Callback debit 回调返回数据 params:{}", object);
        return object;
    }

}
