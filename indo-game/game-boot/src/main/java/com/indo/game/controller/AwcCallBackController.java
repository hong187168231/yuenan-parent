package com.indo.game.controller;

import com.alibaba.fastjson.JSONObject;
import com.indo.game.pojo.entity.awc.AwcApiRequestParentData;
import com.indo.game.service.awc.AwcCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/awcCallBack")
public class AwcCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AwcCallbackService awcAeSexybcrtCallbackService;


    /**
     * 回调
     */
    @RequestMapping(value="/callBack",method=RequestMethod.POST)
    public String initGame(AwcApiRequestParentData awcApiRequestData) {
        logger.info("awcCallBack {} initGame 回调, params:{}",JSONObject.toJSONString(awcApiRequestData));
        String callBack = awcAeSexybcrtCallbackService.awcCallback(awcApiRequestData);
        logger.info("awcCallBack {} initGame 回调返回数据, params:{}",callBack);
        return callBack;
    }
}
