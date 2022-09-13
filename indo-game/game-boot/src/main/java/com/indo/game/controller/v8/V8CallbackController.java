package com.indo.game.controller.v8;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.V8Encrypt;
import com.indo.game.service.v8.V8CallbackService;
import com.indo.game.service.v8.V8Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

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
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
        return v8Service.crebit(loginUser, platform, money, ip,countryCode);
    }

    @RequestMapping(value = "/balance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object balance4V8(@LoginUser LoginInfo loginUser,
                         @RequestBody JSONObject params, HttpServletRequest request){
        String ip = IPAddressUtil.getIpAddress(request);
        String platform = params.getString("platform");
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
        return v8Service.balance(loginUser, platform, ip,countryCode);
    }

    @RequestMapping(value = "/callBack", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object callBack(@RequestParam Map<String,String> paramMap, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        String agent = paramMap.get("agent");
        String timestamp = paramMap.get("timestamp");
        String param = paramMap.get("param");
        String key = paramMap.get("key");
        logger.info("V8Callback callBack 回调, agent:{}, timestamp:{}, param:{}, key:{}, ip:{}", agent, timestamp, param, key,ip);
        int method = 0;
        String account = null;
        String orderId = null;
        String gameNo = null;
        BigDecimal money = BigDecimal.ZERO;
        try {
            param = V8Encrypt.AESDecrypt(param, OpenAPIProperties.V8_DESKEY,false);
            logger.info("V8Callback 回调 解密后数据, param[]:{}", param);
            String[] params = param.split("&");
            for (String temp : params) {
                if (temp.indexOf("s=") == 0) {
                    method = Integer.valueOf(temp.replace("s=", ""));
                } else if (temp.indexOf("account=") == 0) {
                    account = temp.replace("account=", "");
                } else if (temp.indexOf("money=") == 0) {
                    money = BigDecimal.valueOf(Double.valueOf(temp.replace("money=", "")));
                } else if (temp.indexOf("orderId=") == 0) {
                    orderId = temp.replace("orderId=", "");
                } else if (temp.indexOf("gameNo=") == 0) {
                    gameNo = temp.replace("gameNo=", "");
                }
            }
            // 查询余额
            if (1001 == method) {//查询会员余额接口
                return getBalance(agent, timestamp, account, key, ip,method);
            } else if (1002 == method) {//请求下注接口
                // 上分
                return debit(agent, timestamp, account, key, money, ip,method,orderId,gameNo);
            } else if (1003 == method) {//接收派奬接口
                return settle(agent, timestamp, account, key, money, ip,method,orderId,gameNo);
            }else if (1004 == method) {//接收派奬接口
                return queryStatus(agent, timestamp, account, key, ip,method,orderId);
            }else if (1005 == method) {//取消下注接口
                return cancelBet(agent, timestamp, account, key, money, ip,method,orderId);
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
    private Object getBalance(String agent, String timestamp, String account, String key, String ip,int s) {
        logger.info("V8Callback getBalance 回调, agent:{}, timestamp:{}, account:{}, key:{}, ip:{}, s:{}", agent, timestamp, account, key, ip, s);
        Object object = v8CallbackService.getBalance(agent, timestamp, account, key, ip, s);
        logger.info("V8Callback getBalance 回调返回数据 params:{}", object);
        return object;
    }

    // 下注
    private Object debit(String agent, String timestamp, String account, String key, BigDecimal money, String ip,int s,String orderId,String gameNo) {
        logger.info("V8Callback debit 回调,  agent:{}, timestamp:{}, account:{}, key:{},money:{}, ip:{}, s:{}, orderId:{}, gameNo:{}", agent, timestamp, account, key,money, ip, s, orderId, gameNo);
        Object object = v8CallbackService.debit(agent, timestamp, account, key, money, ip, s, orderId, gameNo);
        logger.info("V8Callback debit 回调返回数据 params:{}", object);
        return object;
    }

    // 结算
    private Object settle(String agent, String timestamp, String account, String key, BigDecimal money, String ip,int s,String orderId,String gameNo) {
        logger.info("V8Callback settle 回调, agent:{}, timestamp:{}, account:{}, key:{},money:{}, ip:{}, s:{}, orderId:{}, gameNo:{}", agent, timestamp, account, key,money, ip, s, orderId, gameNo);
        Object object = v8CallbackService.settle(agent, timestamp, account, key, money, ip, s, orderId, gameNo);
        logger.info("V8Callback settle 回调返回数据 params:{}", object);
        return object;
    }

    // 查询订单
    private Object queryStatus(String agent, String timestamp, String account, String key,  String ip,int s,String orderId) {
        logger.info("V8Callback queryStatus 回调, params:{}, agent:{}, timestamp:{}, account:{}, key:{}, ip:{}, s:{}, orderId:{}", agent, timestamp, account, key, ip, s, orderId);
        Object object = v8CallbackService.queryStatus(agent, timestamp, account, key, ip, s, orderId);
        logger.info("V8Callback queryStatus 回调返回数据 params:{}", object);
        return object;
    }

    // 取消下注接口
    private Object cancelBet(String agent, String timestamp, String account, String key, BigDecimal money, String ip,int s,String orderId) {
        logger.info("V8Callback queryStatus 回调, params:{}, agent:{}, timestamp:{}, account:{}, key:{}, ip:{}, s:{}, orderId:{}", agent, timestamp, account, key, ip, s, orderId);
        Object object = v8CallbackService.cancelBet(agent, timestamp, account, key, money, ip, s, orderId);
        logger.info("V8Callback queryStatus 回调返回数据 params:{}", object);
        return object;
    }

}
