package com.indo.pay.util;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.utils.DateUtils;
import com.indo.common.web.util.http.HttpClient;

import java.util.*;

public class PayEasyTest {


    public static void main(String[] args) {

        Map<String, String> metaSignMap = new TreeMap<>();
        metaSignMap.put("merchantId", "10041");
        metaSignMap.put("orderId", IdUtil.simpleUUID());
        metaSignMap.put("coin", "100.00");
        metaSignMap.put("productId", "1000");
        metaSignMap.put("goods", "indotest");
        metaSignMap.put("attach", "1");
        metaSignMap.put("notifyUrl", "https://www.baidu.com/notify");
        metaSignMap.put("redirectUrl", "https://www.baidu.com");

        String sign = SignMd5Utils.createSmallSign(metaSignMap, "71d6725b0744434da17265ca1ca3a67f");
        metaSignMap.put("sign", sign.toUpperCase());
        JSONObject httpRet = null;
        try {
            Map<String, String> headerMap = new HashMap<>();
            httpRet = HttpClient.doPost("http://pay.ddongame222.com/pay", JSON.toJSONString(metaSignMap));
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(httpRet);
    }


}
