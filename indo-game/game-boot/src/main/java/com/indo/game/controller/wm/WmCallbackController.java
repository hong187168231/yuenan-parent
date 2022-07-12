package com.indo.game.controller.wm;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.PostRawParamsParseUtil;
import com.indo.game.pojo.dto.wm.WmCallBackReq;
import com.indo.game.service.wm.WmCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wm")
public class WmCallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WmCallbackService wmCallbackService;

    @RequestMapping(value = "/callBack", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object callBack(HttpServletRequest request, WmCallBackReq wmCallBackReq) {
        logger.info("wmCallback callBack 回调请求参数, params:{}", JSONObject.toJSONString(wmCallBackReq));
        // 查询余额
        if ("CallBalance".equals(wmCallBackReq.getCmd())){
            logger.info("wmCallback getBalance 回调查询余额请求, params:{}", JSONObject.toJSONString(wmCallBackReq));
            return callBalance(wmCallBackReq, request);
        } else if ("PointInout".equals(wmCallBackReq.getCmd())) {
            logger.info("wmCallback pointInout 回调下注请求,params:{}", JSONObject.toJSONString(wmCallBackReq));
            // 下注,回奖
            return pointInout(wmCallBackReq, request);
        } else if ("TimeoutBetReturn".equals(wmCallBackReq.getCmd())) {
            logger.info("wmCallback timeoutBetReturn 回调回退余额请求, params:{}", JSONObject.toJSONString(wmCallBackReq));
            // 回滚
            return timeoutBetReturn(wmCallBackReq, request);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", 911);
        jsonObject.put("errorMessage", "参数不正确");
        return jsonObject;
    }


    private Object callBalance(WmCallBackReq wmCallBackReq, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);

        Object object = wmCallbackService.getBalance(wmCallBackReq, ip);
        logger.info("wmCallback getBalance 回调查询余额返回数据 params:{}", JSONObject.toJSONString(object));
        return object;
    }

    private Object pointInout(WmCallBackReq wmCallBackReq, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);

        Object object = wmCallbackService.pointInout(wmCallBackReq, ip);
        logger.info("wmCallback pointInout 回调下注返回数据 params:{}", JSONObject.toJSONString(object));
        return object;
    }

    private Object timeoutBetReturn(WmCallBackReq wmCallBackReq, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);

        Object object = wmCallbackService.timeoutBetReturn(wmCallBackReq, ip);
        logger.info("wmCallback timeoutBetReturn 回调回退余额返回数据 params:{}", JSONObject.toJSONString(object));
        return object;
    }
}
