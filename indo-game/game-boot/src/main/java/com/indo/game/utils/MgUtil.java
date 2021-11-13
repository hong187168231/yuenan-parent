package com.indo.game.utils;


import com.alibaba.fastjson.JSONObject;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MgUtil extends HttpCommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(MgUtil.class);
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
    /*
     * 异常的最大重试次数
     * */
    private static final int EXCEPTION_MAX_RETRY = 2;


    /**
     * 需要设置代理POST请求（带json参数）
     *
     * @param proxyIp   代理IP
     * @param port      端口号
     * @param tcp       协议（例：http、https）
     * @param url       请求地址
     * @param paramsMap
     * @param
     * @return
     */
    public static String doProxyPostJson(String proxyIp, int port, String tcp, String url, Map<String, String> paramsMap, String type, Map originParams) {
        // 设置代理IP、端口、协议
        // 创建HttpClientBuilder
        logger.info("mglog doProxyPostJson port {} port {}", proxyIp, port);
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
            logger.error("mglog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",
                    type, e.getMessage(), url, proxyIp, port, paramsString, e);
            return resultString;
        } finally {
            closeHttpClientAndResponse(response, closeableHttpClient, url, paramsString);
        }
        return resultString;
    }
}
