package com.indo.common.utils;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameUtil extends HttpCommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(GameUtil.class);
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

    public static String createSign(Map<String, String> packageParams, String signKey) {
        TreeMap sortedMap = new TreeMap(packageParams);
        StringBuilder toSign = new StringBuilder();
        Iterator i$ = sortedMap.keySet().iterator();

        while (i$.hasNext()) {
            String key = (String) i$.next();
            String value = packageParams.get(key);
            if (StringUtils.isNotEmpty(value)) {
                toSign.append(key + value);
            }
        }

        toSign.append(signKey);
        return DigestUtils.md5Hex(toSign.toString()).toUpperCase();
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

    public static JSONObject getJsonMap(Map<String, List<String>> packageParams) {
        TreeMap sortedMap = new TreeMap(packageParams);
        Iterator i$ = sortedMap.keySet().iterator();

        Map<String, Map<String, String[]>> map = new TreeMap<>();

        while (i$.hasNext()) {
            String key = (String) i$.next();
            List<String> value = packageParams.get(key);
            if (null != value && value.size() > 0) {
                Map<String, String[]> map1 = new TreeMap<>();
                for (int i = 0; i < value.size(); i++) {
                    String str[] = new String[]{"ALL"};
                    map1.put(value.get(i), str);
                }
                map.put(key, map1);
            }
        }
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(map);
        return jsonObject;
    }

    /**
     * 加密
     */
    public static String encrypt(String sSrc, String aeskey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = aeskey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        //此处使用BASE64做转码。
        return URLEncoder.encode(Base64.getEncoder().encodeToString(encrypted), "UTF-8");
    }

    /**
     * 解密
     */
    public static String decrypt(String sSrc, String aeskey) {
        try {
            sSrc = sSrc.replaceAll(" ", "+");
            byte[] raw = aeskey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64解密
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            logger.error("decrypt error", ex);
            return null;
        }
    }


    /**
     * 需要设置代理POST请求（带json参数）
     *
     * @param url 请求地址
     */
    public static String doProxyPostJson(String proxyHostName, int proxyPort, String proxyTcp, String url, Map<String, String> paramsMap, String type, Integer userId) {
        // 设置代理IP、端口、协议
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 依次是代理地址，代理端口号，协议类型
//        HttpHost proxy = new HttpHost(proxyHostName, proxyPort, proxyTcp);

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
//                    .setProxy(proxy)
                    .build();
            //如果访问一个接口,多少时间内无法返回数据,就直接放弃此次调用。
            httpPost.setConfig(requestConfig);
            //不复用TCP SOCKET
            httpPost.setHeader("connection", "close");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            List<BasicNameValuePair> list = new ArrayList<>();
            for (String key : paramsMap.keySet()) {
                list.add(new BasicNameValuePair(key, String.valueOf(paramsMap.get(key))));
            }
            logger.info("POST请求 commonRequest userId:{},paramsMap:{}", userId, paramsMap);
            // 创建请求内容
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "utf-8");
            httpPost.setEntity(urlEncodedFormEntity);

            //log参数
            paramsString = JSONObject.toJSONString(paramsMap);
            logger.info("POST请求 commonRequest userId:{},paramsString:{}", userId, paramsString);
            closeableHttpClient = httpClientBuilder.build();
            // 执行http请求
            response = closeableHttpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            resultString = EntityUtils.toString(entity, "utf-8");
            // 此句关闭了流
            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.error("httplog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",
                    userId, type, e.getMessage(), url, paramsString, e);
            return resultString;
        } finally {
            HttpCommonUtils.closeHttpClientAndResponse(response, closeableHttpClient, url, paramsString);
        }
        return resultString;
    }


    public static String doProxyPostJson(String proxyHostName, int proxyPort, String proxyTcp, String weather_url, String json, String type, Integer userId) {
//        return GameUtil.httpProxy(weather_url,json,proxyHostName,proxyPort);
        // 设置代理IP、端口、协议
        // 创建HttpClientBuilder
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 依次是代理地址，代理端口号，协议类型
//        HttpHost proxy = new HttpHost(proxyHostName, proxyPort, proxyTcp);

        CloseableHttpResponse response = null;
        String resultString = "";
        String paramsString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(weather_url);
            // 设置请求超时 20+10+25=55s 配合业务设置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间,单位毫秒。
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    // 从连接池获取到连接的超时,单位毫秒。
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    // 请求获取数据的超时时间,单位毫秒;
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    //设置代理
//                    .setProxy(proxy)
                    .build();
            //如果访问一个接口,多少时间内无法返回数据,就直接放弃此次调用。
            httpPost.setConfig(requestConfig);
            //不复用TCP SOCKET
//            httpPost.setHeader("connection", "close");
//            httpPost.setHeader("Content-Type", "application/json");
//            List<BasicNameValuePair> list = new ArrayList<>();
//            for (String key : paramsMap.keySet()) {
//                list.add(new BasicNameValuePair(key, String.valueOf(paramsMap.get(key))));
//            }

            // 创建请求内容
//            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "utf-8");
//            httpPost.setEntity(urlEncodedFormEntity);
            StringEntity stringEntityentity = new StringEntity(json, "utf-8");//解决中文乱码问题
            stringEntityentity.setContentEncoding("UTF-8");
            stringEntityentity.setContentType("application/json");
            httpPost.setEntity(stringEntityentity);

            //log参数
//            paramsString = JSONObject.toJSONString(paramsMap);
            // 执行http请求
            BasicResponseHandler handler = new BasicResponseHandler();
            resultString = httpClient.execute(httpPost, handler);
//            // 执行http请求
//            response = closeableHttpClient.execute(httpPost);
//            HttpEntity entity = response.getEntity();
//            resultString = EntityUtils.toString(entity, "utf-8");
//            // 此句关闭了流
//            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.error("httplog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",
                    userId, type, e.getMessage(), weather_url, paramsString, e);
            return resultString;
        } finally {
            HttpCommonUtils.closeHttpClientAndResponse(response, httpClient, weather_url, paramsString);
        }
        return resultString;
    }

    public static String doProxyPostJson(String proxyHostName, int proxyPort, String proxyTcp, String weather_url, String json, String type) {
//        return GameUtil.httpProxy(weather_url,json,proxyHostName,proxyPort);
        // 设置代理IP、端口、协议
        // 创建HttpClientBuilder
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 依次是代理地址，代理端口号，协议类型
//        HttpHost proxy = new HttpHost(proxyHostName, proxyPort, proxyTcp);

        CloseableHttpResponse response = null;
        String resultString = "";
        String paramsString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(weather_url);
            // 设置请求超时 20+10+25=55s 配合业务设置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间,单位毫秒。
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    // 从连接池获取到连接的超时,单位毫秒。
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    // 请求获取数据的超时时间,单位毫秒;
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    //设置代理
//                    .setProxy(proxy)
                    .build();
            //如果访问一个接口,多少时间内无法返回数据,就直接放弃此次调用。
            httpPost.setConfig(requestConfig);
            //不复用TCP SOCKET
//            httpPost.setHeader("connection", "close");
//            httpPost.setHeader("Content-Type", "application/json");
//            List<BasicNameValuePair> list = new ArrayList<>();
//            for (String key : paramsMap.keySet()) {
//                list.add(new BasicNameValuePair(key, String.valueOf(paramsMap.get(key))));
//            }

            // 创建请求内容
//            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "utf-8");
//            httpPost.setEntity(urlEncodedFormEntity);
            StringEntity stringEntityentity = new StringEntity(json, "utf-8");//解决中文乱码问题
            stringEntityentity.setContentEncoding("UTF-8");
            stringEntityentity.setContentType("application/json");
            httpPost.setEntity(stringEntityentity);

            //log参数
//            paramsString = JSONObject.toJSONString(paramsMap);
            // 执行http请求
            BasicResponseHandler handler = new BasicResponseHandler();
            resultString = httpClient.execute(httpPost, handler);
//            // 执行http请求
//            response = closeableHttpClient.execute(httpPost);
//            HttpEntity entity = response.getEntity();
//            resultString = EntityUtils.toString(entity, "utf-8");
//            // 此句关闭了流
//            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.error("httplog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",
                    type, e.getMessage(), weather_url, paramsString, e);
            return resultString;
        } finally {
            HttpCommonUtils.closeHttpClientAndResponse(response, httpClient, weather_url, paramsString);
        }
        return resultString;
    }


    public static String httpProxy(String url, String param, String proxy, int port) {
        HttpURLConnection httpConn = null;
        PrintWriter out = null;
        OutputStreamWriter out1 = null;
        BufferedReader in = null;
        String result = "";
        BufferedReader reader = null;
        try {
            URL urlClient = new URL(url);
            logger.error("request URL:{}", url);
            //创建代理
            Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, port));
            //设置代理
            httpConn = (HttpURLConnection) urlClient.openConnection(proxy1);
            //设置通用属性
            httpConn.setRequestProperty("accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");//设置与服务器保持连接
            httpConn.setRequestProperty("Charset", "UTF-8");//设置字符编码类型

            //发送POST请求必须设置如下
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-type", "application/x-javascript->json");//json格式数据

            out1 = new OutputStreamWriter(httpConn.getOutputStream(), "utf-8");
            out1.write(param);
            out1.flush();

            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));

            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            //断开连接
            httpConn.disconnect();
            logger.error("request result:{}", result);
            logger.error("request result:{}", httpConn.getResponseCode(), httpConn.getResponseMessage());
        } catch (Exception e) {
            logger.error("request Exception:{}", e);
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out1 != null) {
                    out1.close();
                }
            } catch (Exception e) {
                logger.error("request finally Exception:{}", e);
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String doProxyPostHeaderJson(String proxyHostName, int proxyPort, String proxyTcp, String url, Map<String, String> paramsMap, String type, Integer userId, String header) {
        // 设置代理IP、端口、协议
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 依次是代理地址，代理端口号，协议类型
//        HttpHost proxy = new HttpHost(proxyHostName, proxyPort, proxyTcp);

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
//                    .setProxy(proxy)
                    .build();
            //如果访问一个接口,多少时间内无法返回数据,就直接放弃此次调用。
            httpPost.setConfig(requestConfig);
            //不复用TCP SOCKET
            httpPost.setHeader("connection", "close");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Authorization", header);
            List<BasicNameValuePair> list = new ArrayList<>();
            for (String key : paramsMap.keySet()) {
                list.add(new BasicNameValuePair(key, String.valueOf(paramsMap.get(key))));
            }
            logger.info("POST请求 commonRequest userId:{},paramsMap:{}", userId, paramsMap);
            // 创建请求内容
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "utf-8");
            httpPost.setEntity(urlEncodedFormEntity);

            //log参数
            paramsString = JSONObject.toJSONString(paramsMap);
            logger.info("POST请求 commonRequest userId:{},paramsString:{}", userId, paramsString);
            closeableHttpClient = httpClientBuilder.build();
            // 执行http请求
            response = closeableHttpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            resultString = EntityUtils.toString(entity, "utf-8");
            // 此句关闭了流
            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.error("httplog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",
                    userId, type, e.getMessage(), url, paramsString, e);
            return resultString;
        } finally {
            HttpCommonUtils.closeHttpClientAndResponse(response, closeableHttpClient, url, paramsString);
        }
        return resultString;
    }

    /**
     * T9电子API请求
     * @param url
     * @param body
     * @param headers
     * @return
     */
    public static String postJson(String url, Map<String, Object> body, Map<String, String> headers) {
        logger.info("POST请求 postJson url:{}", url);
        logger.info("POST请求 postJson body:{}", body);
        logger.info("POST请求 postJson headers:{}", headers);
        String result = null;
        if (true) {
            String charset = "UTF-8";
            CloseableHttpClient httpClient;
            HttpPost httpPost;
            CloseableHttpResponse response = null;
            try {
                httpClient = HttpConnectionManager.getInstance().getHttpClient();
                httpPost = new HttpPost(url);

                // 设置连接超时,设置读取超时
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(10000)
                        .setSocketTimeout(10000)
                        .build();
                httpPost.setConfig(requestConfig);

                for (Map.Entry entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey().toString(), entry.getValue().toString());
                }

                if (null != body) {
                    // 设置参数
                    StringEntity se = new StringEntity(JSONObject.toJSONString(body), "UTF-8");
                    httpPost.setEntity(se);
                    logger.info("POST请求 postJson body1s:{}", JSONObject.toJSONString(httpPost.getEntity()));
                }
                response = httpClient.execute(httpPost);
                if (response != null) {
                    HttpEntity resEntity = response.getEntity();
                    result = EntityUtils.toString(resEntity, charset);
                    EntityUtils.consume(resEntity); // 此句关闭了流
                }
            } catch (Exception e) {
                logger.error("postJson occur error:{}", e.getMessage(), e);
            } finally {
                closeResponse(response, url, body);
            }

        } else {
            result = "config.properties中 is_test 属性值为false, 若已正确设置请求值，请设置为true后再次测试";
        }
        logger.info("POST请求 postJson result:{}", result);
        return result;
    }
}
