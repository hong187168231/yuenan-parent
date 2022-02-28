package com.indo.game.controller.jdb;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.JDBAESDecrypt;
import com.indo.game.service.jdb.JdbCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/jdb")
public class JdbCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbCallbackService jdbCallbackService;


    /**
     * 回调
     */
    @RequestMapping(value="/callBack",method=RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object callBack(String x, HttpServletRequest request) {
        String jsonStr = null;
        try {
            jsonStr = JDBAESDecrypt.decrypt(x, OpenAPIProperties.JDB_KEY,OpenAPIProperties.JDB_IV);
        } catch (Exception e) {
            logger.info("jdbCallBack {} callBack 解密失败",e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("jdbCallBack {} callBack 回调,IP:"+ip+" params:{}",jsonStr);
        Object object = jdbCallbackService.jdbCallback(jsonStr,ip);
        logger.info("jdbCallBack {} callBack 回调返回数据, params:{}",object);
        return object;
    }
}
