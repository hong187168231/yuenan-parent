package com.indo.game.controller;

import com.alibaba.fastjson.JSONObject;
import com.indo.game.pojo.entity.awc.AwcApiRequestParentData;
import com.indo.game.services.awc.AwcCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/awc")
public class AwcCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AwcCallbackService awcAeSexybcrtCallbackService;


    /**
     * 回调
     */
    @ResponseBody
    @RequestMapping(value="/callBack",method=RequestMethod.POST)
    public String initGame(@RequestBody AwcApiRequestParentData awcApiRequestData) {
        logger.info("awcCallBack {} initGame 回调, params:{}",JSONObject.toJSONString(awcApiRequestData));
        return awcAeSexybcrtCallbackService.awcAeSexybcrtCallback(awcApiRequestData);
    }
}
