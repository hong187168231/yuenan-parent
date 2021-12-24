package com.indo.game.controller.awc;

import com.alibaba.fastjson.JSONObject;
import com.indo.game.common.util.IpUtil;
import com.indo.game.pojo.entity.awc.AwcApiRequestParentData;
import com.indo.game.service.awc.AwcCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/awc")
public class AwcCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AwcCallbackService awcAeSexybcrtCallbackService;


    /**
     * 回调
     */
    @RequestMapping(value="/callBack",method=RequestMethod.POST)
    public String initGame(AwcApiRequestParentData awcApiRequestData, HttpServletRequest request) {
        String ip = IpUtil.getIpAddr(request);
        logger.info("awcCallBack {} callBack 回调,IP:"+ip+" params:{}",JSONObject.toJSONString(awcApiRequestData));

        String callBack = awcAeSexybcrtCallbackService.awcCallback(awcApiRequestData,ip);
        logger.info("awcCallBack {} callBack 回调返回数据, params:{}",callBack);
        return callBack;
    }
}