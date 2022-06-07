package com.indo.common.utils;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

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
    // 连接主机超时（30s）
    public static final int HTTP_CONNECT_TIMEOUT_30S = 30 * 1000;
    // 从主机读取数据超时（3min）
    public static final int HTTP_READ_TIMEOUT_3MIN = 180 * 1000;
    public static final String DEFAULT_CHARSET = "utf-8";
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
            + "(KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";

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
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
        //此处使用BASE64做转码。
        return URLEncoder.encode(Base64.getEncoder().encodeToString(encrypted), StandardCharsets.UTF_8.toString());
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
            String originalString = new String(original, StandardCharsets.UTF_8);
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
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
            httpPost.setEntity(urlEncodedFormEntity);

            //log参数
            paramsString = JSONObject.toJSONString(paramsMap);
            logger.info("POST请求 commonRequest userId:{},paramsString:{}", userId, paramsString);
            closeableHttpClient = httpClientBuilder.build();
            // 执行http请求
            response = closeableHttpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
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
//            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
//            httpPost.setEntity(urlEncodedFormEntity);
            StringEntity stringEntityentity = new StringEntity(json, StandardCharsets.UTF_8);//解决中文乱码问题
            stringEntityentity.setContentEncoding(StandardCharsets.UTF_8.toString());
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
//            resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
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
//            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
//            httpPost.setEntity(urlEncodedFormEntity);
            StringEntity stringEntityentity = new StringEntity(json, StandardCharsets.UTF_8);//解决中文乱码问题
            stringEntityentity.setContentEncoding(StandardCharsets.UTF_8.toString());
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
//            resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
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

    public static String doProxyPostJson(String weather_url, String json, Map<String, String> headers, String type) {
        logger.info("POST请求 doProxyPostJson url:{}", weather_url);
        logger.info("POST请求 doProxyPostJson body:{}", json);
        logger.info("POST请求 doProxyPostJson headers:{}", headers);
        // 创建HttpClientBuilder
        CloseableHttpClient httpClient = HttpClients.createDefault();

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
            for (Map.Entry entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey().toString(), entry.getValue().toString());
            }
            // 创建请求内容
            StringEntity stringEntityentity = new StringEntity(json, StandardCharsets.UTF_8);//解决中文乱码问题
            stringEntityentity.setContentEncoding(StandardCharsets.UTF_8.toString());
            stringEntityentity.setContentType("application/json");
            httpPost.setEntity(stringEntityentity);

            // 执行http请求
            BasicResponseHandler handler = new BasicResponseHandler();
            resultString = httpClient.execute(httpPost, handler);
        } catch (Exception e) {
            logger.error("httplog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",
                    type, e.getMessage(), weather_url, paramsString, e);
            return resultString;
        } finally {
            HttpCommonUtils.closeHttpClientAndResponse(response, httpClient, weather_url, paramsString);
        }
        logger.info("POST请求 doProxyPostJson result:{}", resultString);
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
            httpConn.setRequestProperty("Charset", StandardCharsets.UTF_8.toString());//设置字符编码类型

            //发送POST请求必须设置如下
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-type", "application/x-javascript->json");//json格式数据

            out1 = new OutputStreamWriter(httpConn.getOutputStream(), StandardCharsets.UTF_8);
            out1.write(param);
            out1.flush();

            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), StandardCharsets.UTF_8));

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
            if (!StringUtils.isEmpty(header)) {
                httpPost.setHeader("Authorization", header);
            }
            List<BasicNameValuePair> list = new ArrayList<>();
            for (String key : paramsMap.keySet()) {
                list.add(new BasicNameValuePair(key, String.valueOf(paramsMap.get(key))));
            }
            logger.info("POST请求 commonRequest userId:{},paramsMap:{}", userId, paramsMap);
            // 创建请求内容
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
            httpPost.setEntity(urlEncodedFormEntity);

            //log参数
            paramsString = JSONObject.toJSONString(paramsMap);
            logger.info("POST请求 commonRequest userId:{},paramsString:{}", userId, paramsString);
            closeableHttpClient = httpClientBuilder.build();
            // 执行http请求
            response = closeableHttpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
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
     * kM
     */
    public static String doProxyPostKmJson(String proxyHostName, int proxyPort, String proxyTcp, String url, Map<String, String> paramsMap, String type, Integer userId, String clientId, String clientSecret) {
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
            httpPost.setHeader("X-QM-ClientId", clientId);
            httpPost.setHeader("X-QM-ClientSecret", clientSecret);
            logger.info("POST请求 commonRequest userId:{},paramsMap:{}", userId, paramsMap);

            paramsString = JSONObject.toJSONString(paramsMap);
            StringEntity stringEntityentity = new StringEntity(paramsString, StandardCharsets.UTF_8);//解决中文乱码问题
            stringEntityentity.setContentEncoding(StandardCharsets.UTF_8.toString());
            stringEntityentity.setContentType("application/json");
            httpPost.setEntity(stringEntityentity);
            //log参数
            logger.info("POST请求 commonRequest userId:{},paramsString:{}", userId, paramsString);

            // 执行http请求
            BasicResponseHandler handler = new BasicResponseHandler();
            resultString = httpClient.execute(httpPost, handler);
            // 此句关闭了流
        } catch (Exception e) {
            logger.error("httplog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",
                    userId, type, e.getMessage(), url, paramsString, e);
            return resultString;
        } finally {
            HttpCommonUtils.closeHttpClientAndResponse(response, httpClient, url, paramsString);
        }
        return resultString;
    }

    /**
     * T9电子API请求
     */
    public static String postJson(String url, Map<String, Object> body, Map<String, String> headers) {
        logger.info("POST请求 postJson url:{}", url);
        logger.info("POST请求 postJson body:{}", body);
        logger.info("POST请求 postJson headers:{}", headers);
        String result = null;
        if (true) {
            String charset = StandardCharsets.UTF_8.toString();
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
                    StringEntity se = new StringEntity(JSONObject.toJSONString(body), StandardCharsets.UTF_8);
                    httpPost.setEntity(se);
                }
                response = httpClient.execute(httpPost);
                if (response != null) {
                    logger.info("POST请求 postJson status: {}", JSONObject.toJSONString(response.getStatusLine()));
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


    /**
     * PP电子 form表单请求
     */
    public static String postForm4PP(String url, Map<String, Object> body, String method) {
        logger.info("POST请求 postJson url:{}, method: {}", url, method);
        logger.info("POST请求 postJson body:{}", body);
        String result = null;
        if (true) {
            String charset = StandardCharsets.UTF_8.toString();
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

                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

                if (null != body) {
                    List<BasicNameValuePair> list = new ArrayList<>();
                    for (String key : body.keySet()) {
                        list.add(new BasicNameValuePair(key, String.valueOf(body.get(key))));
                    }
                    // 创建请求内容
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
                    httpPost.setEntity(urlEncodedFormEntity);
                    logger.info("POST请求 postJson ,urlEncodedFormEntity:{}", JSONObject.toJSONString(urlEncodedFormEntity));

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

    public static String httpGetWithCookies(String url, Header[] headers, HttpHost proxy) {
        logger.info("GET请求 httpGetWithCookies headers:{}, proxy: {}", headers, proxy);
        logger.info("GET请求 httpGetWithCookies url:{}", url);
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(150);
        connManager.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(Charset.forName("utf-8")).build());
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(30000).setSoReuseAddress(true).build();
        connManager.setDefaultSocketConfig(socketConfig);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();

        // 超时时间设置
        HttpGet httpGet = new HttpGet(url);
        RequestConfig.Builder requestConfigBuilder = buildDefaultRequestConfigBuilder();
        if (Objects.nonNull(proxy)) {
            requestConfigBuilder.setProxy(proxy);
        }
        httpGet.setConfig(requestConfigBuilder.build());
        httpGet.setHeader("User-agent", DEFAULT_USER_AGENT);
        if (headers != null && headers.length > 0) {
            httpGet.setHeaders(headers);
        }
        long start = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, DEFAULT_CHARSET);
            EntityUtils.consume(entity); // 此句关闭了流
            logger.info("GET请求 httpGetWithCookies result:{}", result);
            return result;
        } catch (Exception e) {
            logger.error("请求的接口为:[{}], 发生异常原因: {}", url, e.getMessage(), e);
            return "";
        } finally {

            closeResponse(response, url, null);
            logger.error("httpGetWithCookies请求的地址为:[{}], 总共耗时：{} ms", url,
                    (LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() - start));
        }
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    private static RequestConfig.Builder buildDefaultRequestConfigBuilder() {
        return RequestConfig.custom()
                .setSocketTimeout(HTTP_READ_TIMEOUT_3MIN)
                .setConnectTimeout(HTTP_CONNECT_TIMEOUT_30S);
    }
}
