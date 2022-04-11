package com.indo.game.controller.mt;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.service.mt.MtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping("/mt")
public class MtController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MtService mtService;

    @RequestMapping(value = "/allWithdraw", method = RequestMethod.POST)
    @AllowAccess
    public Object allWithdraw(@LoginUser LoginInfo loginUser,
                              @RequestBody JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("MtController allWithdraw, params:{}", params);
        String platform = params.getString("platform");
        Object object = mtService.allWithdraw(loginUser, platform, ip);
        logger.info("MtController allWithdraw 返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/getPlayerBalance", method = RequestMethod.POST)
    @AllowAccess
    private Object getPlayerBalance(@LoginUser LoginInfo loginUser,
                                    @RequestBody JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("MtController getPlayerBalance , params:{}", params);
        String platform = params.getString("platform");
        Object object = mtService.getPlayerBalance(loginUser, platform, ip);
        logger.info("MtController getPlayerBalance 返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    @AllowAccess
    private Object deposit(@LoginUser LoginInfo loginUser,
                           @RequestBody JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("MtController deposit params:{}", params);
        String platform = params.getString("platform");
        BigDecimal coins = params.getBigDecimal("coins");
        Object object = mtService.deposit(loginUser, platform, ip, coins);
        logger.info("MtController deposit 返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    @AllowAccess
    private Object withdraw(@LoginUser LoginInfo loginUser,
                            @RequestBody JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("MtController withdraw params:{}", params);
        String platform = params.getString("platform");
        BigDecimal coins = params.getBigDecimal("coins");
        Object object = mtService.withdraw(loginUser, platform, ip, coins);
        logger.info("MtController withdraw 返回数据 params:{}", object);
        return object;
    }

}
