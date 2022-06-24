package com.indo.game.controller.bl;

import com.alibaba.fastjson.JSON;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.bl.BlCallBackReq;
import com.indo.game.service.bl.BlCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bl/callBack")
public class BlCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BlCallbackService blCallbackService;


    /**
     * 查询余额
     */
    @RequestMapping(value = "/player/balance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @AllowAccess
    @ResponseBody
    public Object balance(@RequestBody BlCallBackReq blCallBackReq,
                          @RequestBody HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("blCallBack  getBalance 回调下注params:{},ip{}", JSON.toJSON(blCallBackReq),ip);
        Object object = blCallbackService.blBlanceCallback(blCallBackReq, ip);
        logger.info("blCallBack getBalance 回调下注返回数据 取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 扣款接口
     */
    @RequestMapping(value = "/player/cost", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @AllowAccess
    @ResponseBody
    public Object player(@RequestBody BlCallBackReq blCallBackReq,
                         @RequestBody HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("blCallBack  player回调 params:{},ip{}", JSON.toJSON(blCallBackReq),ip);
        Object object = blCallbackService.blPlayerCallback(blCallBackReq, ip);
        logger.info("blCallBack  player回调返回数据  params:{}", object);
        return object;
    }

}
