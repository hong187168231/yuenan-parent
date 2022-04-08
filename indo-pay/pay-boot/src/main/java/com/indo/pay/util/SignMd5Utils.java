package com.indo.pay.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;


public class SignMd5Utils {

    private final static Logger log = LoggerFactory.getLogger(SignMd5Utils.class);


    /**
     * @return java.lang.String
     * @Author aguang
     * @Description 进行MD5加密
     * @Date 11:19 2021/2/25
     * @Param [str, isUpperCase 是否转 大写  true：大写，false：小写]
     **/
    public static String MD5Hex(String str, boolean isUpperCase) {
        if (isUpperCase) {
            str = DigestUtils.md5Hex(str).toUpperCase();
        } else {
            str = DigestUtils.md5Hex(str).toLowerCase();
        }
        return str;
    }


    /**
     * 生成签名 大写风格 固定顺序 JSON字符串
     */
    public static String createSignLinkedJson(LinkedHashMap<String, String> packageParams, String signKey) {
        String signString = com.alibaba.fastjson.JSON.toJSONString(packageParams) + signKey;
        log.info("paylog SignMd5Utils createSignLinkedJson 生成签名 {}", signString);
        String sign = DigestUtils.md5Hex(signString).toUpperCase();
        log.info("paylog SignMd5Utils createSignLinkedJson 大写签名 {}", sign);
        return sign;
    }

    /**
     * 生成签名 大写风格 固定顺序
     */
    public static String createSignLinked(LinkedHashMap<String, String> packageParams, String signKey, String lastAnd) {
        String sign = createSmallSignLinked(packageParams, signKey, lastAnd).toUpperCase();
        log.info("paylog SignMd5Utils createSignLinked 大写签名 {}", sign);
        return sign;
    }

    /**
     * 生成签名 小写风格 固定顺序
     */
    public static String createSmallSignLinked(LinkedHashMap<String, String> packageParams, String signKey, String lastAnd) {
        StringBuilder toSign = new StringBuilder();
        for (Map.Entry<String, String> entry : packageParams.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }
        toSign = toSign.deleteCharAt(toSign.length() - 1);
        toSign.append(lastAnd);
        toSign.append(signKey);
        log.info("paylog SignMd5Utils createSmallSignLinked 生成签名 {}", toSign.toString());
        String sign = DigestUtils.md5Hex(toSign.toString());
        log.info("paylog SignMd5Utils createSmallSignLinked 小写签名 {}", sign);
        return sign;
    }

    /**
     * 生成签名 大写风格
     */
    public static String createSign(Map<String, String> packageParams, String signKey) {
        return createSmallSign(packageParams, signKey).toUpperCase();
    }

    /**
     * 生成签名 小写风格
     */
    public static String createSmallSign(Map<String, String> packageParams, String signKey) {
        TreeMap sortedMap = new TreeMap(packageParams);
        StringBuilder toSign = new StringBuilder();
        Iterator it = sortedMap.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            String value = packageParams.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }

        toSign.append("key=" + signKey);
        log.info("paylog SignMd5Utils createSmallSign 生成签名 {}", toSign.toString());
        String sign = DigestUtils.md5Hex(toSign.toString());
        log.info("paylog SignMd5Utils createSmallSign 小写签名 {}", sign);
        return sign;
    }


    /**
     * 检查签名
     */
    public static boolean checkSign(Map<String, String> packageParams, String signKey, String sign) {
        String newSign = createSign(packageParams, signKey);

        if (newSign.equalsIgnoreCase(sign)) {
            return true;
        } else {
            return false;
        }
    }


    public static String createSignMd5(Map<String, String> packageParams, String signKey) {
        TreeMap sortedMap = new TreeMap(packageParams);
        StringBuilder toSign = new StringBuilder();
        Iterator i$ = sortedMap.keySet().iterator();

        while (i$.hasNext()) {
            String key = (String) i$.next();
            String value = (String) packageParams.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }
        String str = toSign.toString();
        str = str.substring(0, str.length() - 1);
        StringBuilder toSignMdBuilder = new StringBuilder();
        toSignMdBuilder.append(str).append(signKey);
        return DigestUtils.md5Hex(toSignMdBuilder.toString());
    }


    /**
     * Srust 拼接
     */
    public static String createSrustSign(Map<String, String> packageParams) {
        TreeMap sortedMap = new TreeMap(packageParams);
        StringBuilder toSign = new StringBuilder();
        Iterator i$ = sortedMap.keySet().iterator();

        while (i$.hasNext()) {
            String key = (String) i$.next();
            String value = (String) packageParams.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }
        String str = toSign.toString();
        str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * Srust RSA
     */
    public static String sign(String info, String priKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(priKey);
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
            Signature signet = Signature.getInstance("SHA1withRSA");
            signet.initSign(myprikey);
            byte[] infoByte = info.getBytes("UTF-8");
            signet.update(infoByte);
            byte[] signed = signet.sign();
            String sign = new BASE64Encoder().encode((signed));
            System.out.println("sign = " + sign);
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Srust 验证
     */
    public static Boolean verifySign(String info, String pubkey, String sign) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(pubkey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicK);
            byte[] infoByte = info.getBytes("UTF-8");
            signature.update(infoByte);
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(false);
    }


    public static String createParamSign(Map<String, String> packageParams) {
        TreeMap sortedMap = new TreeMap(packageParams);
        StringBuilder toSign = new StringBuilder();
        Iterator i$ = sortedMap.keySet().iterator();

        while (i$.hasNext()) {
            String key = (String) i$.next();
            String value = (String) packageParams.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }
        String str = toSign.toString();
        str = str.substring(0, str.length() - 1);
        return str;
    }


    public static String createSignMd5toUpperCase(Map<String, String> packageParams, String signKey) {
        TreeMap sortedMap = new TreeMap(packageParams);
        StringBuilder toSign = new StringBuilder();
        Iterator i$ = sortedMap.keySet().iterator();

        while (i$.hasNext()) {
            String key = (String) i$.next();
            String value = (String) packageParams.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key + value);
            }
        }
        toSign.append(signKey);
        return DigestUtils.md5Hex(toSign.toString()).toUpperCase();
    }

    /**
     * 生成签名
     */
    public static String createSignAppendSign(Map<String, String> packageParams, String signKey) {
        TreeMap sortedMap = new TreeMap(packageParams);
        StringBuilder toSign = new StringBuilder();
        Iterator i$ = sortedMap.keySet().iterator();

        while (i$.hasNext()) {
            String key = (String) i$.next();
            String value = (String) packageParams.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }
        String str = toSign.toString();
        str = str.substring(0, str.length() - 1);
        String stringSignTemp = str + signKey;
        return DigestUtils.md5Hex(stringSignTemp).toUpperCase();
    }


    /**
     * 生成签名
     */
    public static String createSignAppendKey(Map<String, String> packageParams, String signKey) {
        TreeMap sortedMap = new TreeMap(packageParams);
        StringBuilder toSign = new StringBuilder();
        Iterator i$ = sortedMap.keySet().iterator();

        while (i$.hasNext()) {
            String key = (String) i$.next();
            String value = (String) packageParams.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }
        String str = toSign.toString();
        str = str.substring(0, str.length() - 1);
        String stringSignTemp = str + "&key=" + signKey;
        return DigestUtils.md5Hex(stringSignTemp).toUpperCase();
    }


    public static String paramsSign(String params) {
        return DigestUtils.md5Hex(params);
    }


    /**
     * 生成签名
     */
    public static String getSign(Map<String, String> map) {

        String result = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });

            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (!(val == "" || val == null)) {
                        sb.append(key + "=" + val + "&");
                    }
                }

            }
            result = sb.toString();
            result = result.substring(0, result.length() - 1);

            //进行MD5加密
            result = DigestUtils.md5Hex(result).toUpperCase();
        } catch (Exception e) {
            return null;
        }
        return result;
    }


    /**
     * 生成签名
     */
    public static String getSignKey(Map<String, String> map, String apiKey) {

        String result = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });

            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (!(val == "" || val == null)) {
                        sb.append(key + "=" + val + "&");
                    }
                }

            }
            result = sb.toString();
            result = result + "key=" + apiKey;
            //进行MD5加密
            result = DigestUtils.md5Hex(result).toUpperCase();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * best 64
     */
    public static String base64(String str, String code) {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = str.getBytes();
        //Base64 加密
        String encoded = Base64.getEncoder().encodeToString(bytes);
        stringBuilder.append(encoded).append(code);
        return stringBuilder.toString();
    }

}
