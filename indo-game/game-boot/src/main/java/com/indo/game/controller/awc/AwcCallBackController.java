package com.indo.game.controller.awc;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.awc.AwcApiRequestParentData;
import com.indo.game.service.awc.AwcCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/awc")
public class AwcCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AwcCallbackService awcAeSexybcrtCallbackService;


    /**
     * 回调
     */
    @RequestMapping(value="/callBack",method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object initGame(AwcApiRequestParentData awcApiRequestData, HttpServletRequest request) {

        logger.info("开始时间 Date:{}", DateUtils.format(new Date(),DateUtils.newFormat));
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("awcCallBack  initGame 回调请求数据,IP:{} params:{}",ip,JSONObject.toJSONString(awcApiRequestData));

        Object object = awcAeSexybcrtCallbackService.awcCallback(awcApiRequestData,ip);
        logger.info("awcCallBack  initGame 回调返回数据, params:{}",object);
        logger.info("结束时间 Date:{}", DateUtils.format(new Date(),DateUtils.newFormat));
        return object;
    }
}
