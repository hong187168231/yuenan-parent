package com.indo.game.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DbUtil extends HttpCommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(DbUtil.class);
    /**
     * 设置连接超时时间,单位毫秒
     */
    private static final int CONNECT_TIMEOUT = 10000;
    /*
     * 从连接池获取到连接的超时,单位毫秒
     * */
    private static final int CONNECTION_REQUEST_TIMEOUT = 3000;
    /*
     * 请求获取数据的超时时间,单位毫秒
     * */
    private static final int SOCKET_TIMEOUT = 7000;

    public static String encrypt(String data, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        int blockSize = cipher.getBlockSize();
        byte[] dataBytes = data.getBytes("UTF-8");
        int plainTextLength = dataBytes.length;
        if (plainTextLength % blockSize != 0) {
            plainTextLength = plainTextLength + (blockSize - plainTextLength % blockSize);
        }
        byte[] plaintext = new byte[plainTextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(plaintext);
        return new BASE64Encoder().encode(encrypted);
    }


    public static JSONObject sortMap(Map<String, String> packageParams) {
        TreeMap sortedMap = new TreeMap(packageParams);
        Iterator i$ = sortedMap.keySet().iterator();

        Map<String, String> map = new TreeMap<>();
        while (i$.hasNext()) {
            String key = (String) i$.next();
            String value = packageParams.get(key);
            if (StringUtils.isNotEmpty(value)) {
                map.put(key, value);
            }
        }
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(map);
        return jsonObject;
    }


    /**
     * 需要设置代理POST请求（带json参数）
     *
     * @param proxyIp 代理IP
     * @param port    端口号
     * @param tcp     协议（例：http、https）
     * @param url     请求地址
     */
    public static String doProxyPostJson(String proxyIp, int port, String proxyHost, String tcp, String url, Map<String, String> paramsMap, String type, Map originParams) {
        // 设置代理IP、端口、协议
        logger.info("dblog doProxyPostJson proxyIp {} port {} proxyHost {},", proxyIp, port, proxyHost);
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 依次是代理地址，代理端口号，协议类型
        HttpHost proxy = new HttpHost(proxyIp, port, tcp);

        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse response = null;
        String resultString = "";
        String paramsString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 设置请求超时 20+10+25=55s 配合业务设置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间,单位毫秒。
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    // 从连接池获取到连接的超时,单位毫秒。
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    // 请求获取数据的超时时间,单位毫秒;
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    //设置代理
                    .setProxy(proxy)
                    .build();
            //如果访问一个接口,多少时间内无法返回数据,就直接放弃此次调用。
            httpPost.setConfig(requestConfig);
            //不复用TCP SOCKET
            httpPost.setHeader("connection", "close");
            httpPost.setHeader("X-Forwarded-For", proxyHost);
            List<BasicNameValuePair> list = new ArrayList<>();
            for (String key : paramsMap.keySet()) {
                list.add(new BasicNameValuePair(key, String.valueOf(paramsMap.get(key))));
            }

            // 创建请求内容
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "utf-8");
            httpPost.setEntity(urlEncodedFormEntity);

            //log参数
            paramsString = JSONObject.toJSONString(originParams);
            closeableHttpClient = httpClientBuilder.build();
            // 执行http请求
            response = closeableHttpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            resultString = EntityUtils.toString(entity, "utf-8");
            // 此句关闭了流
            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.error("dblog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",
                    type, e.getMessage(), url, proxyIp, port, paramsString, e);
            return resultString;
        } finally {
            closeHttpClientAndResponse(response, closeableHttpClient, url, paramsString);
        }
        return resultString;
    }
}
