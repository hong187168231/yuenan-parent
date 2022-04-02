package com.indo.game.controller.cmd;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.service.cmd.CmdCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cmd")
public class CmdCallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CmdCallbackService cmdCallbackService;

    @RequestMapping(value = "/callBack", method = RequestMethod.GET)
    @AllowAccess
    public Object check(@RequestParam("token") String token,
                        @RequestParam("secret_key") String secretKey,
                        HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("cmdCallback check 回调验证, params:{}, {}", token, secretKey);
        Object object = cmdCallbackService.check(token, secretKey, ip);
        logger.info("cmdCallback check 回调验证返回数据 params:{}", object);
        return object;
    }


    @RequestMapping(value = "/callBack/getBalance", method = RequestMethod.GET)
    @AllowAccess
    public Object getBalance(@RequestParam("balancePackage") String balancePackage,
                             @RequestParam("packageId") String packageId,
                             @RequestParam("dateSent") String dateSent,
                             HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("cmdCallback getBalance 回调查询余额, params:{}", balancePackage);
        Object object = cmdCallbackService.getBalance(balancePackage, packageId, dateSent, ip);
        logger.info("cmdCallback getBalance 回调查询余额返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/callBack/deductBalance", method = RequestMethod.POST)
    @AllowAccess
    public Object deductBalance(@RequestBody JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("cmdCallback deductBalance 回调下注params:{}", params);
        Object object = cmdCallbackService.deductBalance(params, ip);
        logger.info("cmdCallback deductBalance 回调下注返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/callBack/updateBalance", method = RequestMethod.POST)
    @AllowAccess
    public Object updateBalance(@RequestBody JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("cmdCallback updateBalance 回调更新余额 params:{}", params);
        Object object = cmdCallbackService.updateBalance(params, ip);
        logger.info("cmdCallback updateBalance 回调更新余额返回数据 params:{}", object);
        return object;
    }

}
