package com.indo.pay.util;

import cn.hutool.core.util.IdUtil;
import com.indo.common.utils.DateUtils;
import com.indo.common.web.util.http.HttpClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PayTest {


    public static void main(String[] args) {

        Map<String, String> metaSignMap = new TreeMap<>();
        metaSignMap.put("version", "1.0");
        metaSignMap.put("mch_id", "101103004");
        metaSignMap.put("notify_url", "www.baidu.com");
        metaSignMap.put("page_url", "www.baidu.com");
        metaSignMap.put("mch_order_no", IdUtil.simpleUUID());
        metaSignMap.put("pay_type", "102");
        metaSignMap.put("trade_amount", "100");
        metaSignMap.put("order_date", DateUtils.getNewFormatDateString(new Date()));
        metaSignMap.put("bank_code", "IDPT0001");
        metaSignMap.put("goods_name", "indotest");
        metaSignMap.put("payer_phone", "855973559275");
        String sign = SignMd5Utils.createSmallSign(metaSignMap, "47f57ddf74bb4e00b61b0ef96130fb8b");
        metaSignMap.put("sign_type", "MD5");
        metaSignMap.put("sign", sign);
        String httpRet = "";
        try {
            httpRet = HttpClient.formPost("https://pay.huarenpay.top/pay/web", metaSignMap);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(111);
    }


}
