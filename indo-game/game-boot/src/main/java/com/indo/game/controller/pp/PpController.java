package com.indo.game.controller.pp;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.pp.PpApiGetBalanceReq;
import com.indo.game.pojo.dto.pp.PpApiStartGameReq;
import com.indo.game.pojo.dto.pp.PpApiTransferReq;
import com.indo.game.service.pp.PpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 提供PP电子游戏启动、余额查询以及资金存转
 */
@RestController
@RequestMapping("/pp")
public class PpController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PpService ppService;

    /**
     * 余额存转
     */
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object transfer(PpApiTransferReq ppApiTransferReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppController {} transfer ,params:{}", JSONObject.toJSONString(ppApiTransferReq));
        Object object = ppService.transfer(ppApiTransferReq, ip);
        logger.info("ppController {} transfer 余额存取返回数据 params:{}", JSONObject.toJSONString(object));
        return object;
    }

    /**
     * 获取PP电子余额
     */
    @RequestMapping(value = "/getBalance", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object getBalance(PpApiGetBalanceReq ppApiGetBalanceReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppController {} getBalance ,params:{}", JSONObject.toJSONString(ppApiGetBalanceReq));
        Object object = ppService.getBalance(ppApiGetBalanceReq, ip);
        logger.info("ppController {} getBalance 获取余额返回数据 params:{}", JSONObject.toJSONString(object));
        return object;
    }

    /**
     * 启动游戏
     */
    @RequestMapping(value = "/startGame", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object startGame(PpApiStartGameReq ppApiStartGameReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ppController {} startGame ,params:{}", JSONObject.toJSONString(ppApiStartGameReq));
        Object object = ppService.startGame(ppApiStartGameReq, ip);
        logger.info("ppController {} startGame 启动游戏返回数据 params:{}", JSONObject.toJSONString(object));
        return object;
    }
}
