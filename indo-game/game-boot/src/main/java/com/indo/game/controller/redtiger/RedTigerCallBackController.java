package com.indo.game.controller.redtiger;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.service.redtiger.RedtigerCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/RT/callBack")
public class RedTigerCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedtigerCallbackService redtigerCallbackService;


    /**
     * 令牌验证
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object check(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("redtigerCallBack check 回调,params:{}", JSONObject.toJSONString(map));
        Object object = redtigerCallbackService.check(map, ip);
        logger.info("redtigerCallBack check 回调权限验证返回数据 params:{}", object);
        return object;
    }

    /**
     * 查询余额
     */
    @RequestMapping(value = "/balance", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object balance(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("redtigerCallBack balance 回调,params:{}", JSONObject.toJSONString(map));
        Object object = redtigerCallbackService.balance(map, ip);
        logger.info("redtigerCallBack balance 回调查询余额返回数据 params:{}", object);
        return object;
    }


    /**
     * 下注
     */
    @RequestMapping(value = "/debit", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object debit(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("redtigerCallBack debit 回调,params:{}", JSONObject.toJSONString(map));
        Object object = redtigerCallbackService.debit(map, ip);
        logger.info("redtigerCallBack debit 回调下注返回数据 params:{}", object);
        return object;
    }

    /**
     * 派奖
     */
    @RequestMapping(value = "/credit", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object credit(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("redtigerCallBack credit 回调,params:{}", JSONObject.toJSONString(map));
        Object object = redtigerCallbackService.credit(map, ip);
        logger.info("redtigerCallBack credit 回调玩家中奖返回数据 params:{}", object);
        return object;
    }

    /**
     * 撤销
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object cancel(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("redtigerCallBack cancel 回调,params:{}", JSONObject.toJSONString(map));
        Object object = redtigerCallbackService.cancel(map, ip);
        logger.info("redtigerCallBack cancel 回调退款返回数据 params:{}", object);
        return object;
    }

    /**
     * 活动派奖
     */
    @RequestMapping(value = "/promo_payout", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object promo_payout(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("redtigerCallBack promo_payout 回调,params:{}", JSONObject.toJSONString(map));
        Object object = redtigerCallbackService.promo_payout(map, ip);
        logger.info("redtigerCallBack promo_payout 回调活动派奖返回数据 params:{}", object);
        return object;
    }

}
