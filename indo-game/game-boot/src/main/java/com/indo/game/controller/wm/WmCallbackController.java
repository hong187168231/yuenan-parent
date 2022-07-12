package com.indo.game.controller.wm;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.PostRawParamsParseUtil;
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
    public Object callBack(HttpServletRequest request) {
        JSONObject params = new JSONObject();
        try {
            logger.info("wmCallback callBack 回调请求参数, params:{}", PostRawParamsParseUtil.getRequestPostBytes(request));
            String[] datas = PostRawParamsParseUtil.getRequestPostBytes(request).split("&");
            for (String data : datas) {
                params.put(data.split("=")[0], data.split("=")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            params = new JSONObject();
        }
        logger.info("wmCallback callBack 回调请求, params:{}", JSONObject.toJSONString(params));
        // 查询余额
        if ("CallBalance".equals(params.getString("cmd"))) {
            logger.info("wmCallback getBalance 回调查询余额请求, params:{}", JSONObject.toJSONString(params));
            return callBalance(params, request);
        } else if ("PointInout".equals(params.getString("cmd"))) {
            logger.info("wmCallback pointInout 回调下注请求,params:{}", JSONObject.toJSONString(params));
            // 下注,回奖
            return pointInout(params, request);
        } else if ("TimeoutBetReturn".equals(params.getString("cmd"))) {
            logger.info("wmCallback timeoutBetReturn 回调回退余额请求, params:{}", JSONObject.toJSONString(params));
            // 回滚
            return timeoutBetReturn(params, request);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", 911);
        jsonObject.put("errorMessage", "参数不正确");
        return jsonObject;
    }


    private Object callBalance(JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);

        Object object = wmCallbackService.getBalance(params, ip);
        logger.info("wmCallback getBalance 回调查询余额返回数据 params:{}", JSONObject.toJSONString(object));
        return object;
    }

    private Object pointInout(JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);

        Object object = wmCallbackService.pointInout(params, ip);
        logger.info("wmCallback pointInout 回调下注返回数据 params:{}", JSONObject.toJSONString(object));
        return object;
    }

    private Object timeoutBetReturn(JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);

        Object object = wmCallbackService.timeoutBetReturn(params, ip);
        logger.info("wmCallback timeoutBetReturn 回调回退余额返回数据 params:{}", JSONObject.toJSONString(object));
        return object;
    }

    public static void main(String args[]){
        String[] datas = "cmd=CallBalance&user=swuserid&signature=0ae2ecac3fd0bc37bc70f278cf875250&requestDate=2022-07-12+16%3A27%3A15".split("&");
        for (String data : datas) {
            System.out.println(data.split("=")[0]+"------------"+data.split("=")[1]);
        }
    }
}
