package com.indo.game.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * @author
 */
public class KYUtil extends HttpCommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(KYUtil.class);

    /**
     * 代理编号+yyyyMMddHHmmssSSS+ account,
     *
     * @return
     */
    public static String buildOrderId(String agent, String account) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return agent + format.format(new Date()) + account;
    }

    /**
     * MD5 32位加密
     *
     * @param sourceStr
     * @return
     */
    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5 occur error.", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("MD5 occur error.", e);
        }
        return result;
    }


    /**
     * AES加密
     *
     * @param value
     * @param key
     * @return
     * @throws Exception
     */
    public static String AESEncrypt(String value, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = key.getBytes("UTF-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
        String base64 = new BASE64Encoder().encode(encrypted); // 此处使用BASE64做转码
        return URLEncoder.encode(base64, "UTF-8");// URL加密
    }

    /**
     * AES加密 不进行URLEncoder
     *
     * @param value
     * @param key
     * @return
     * @throws Exception
     */
    public static String AESUNURLEncrypt(String value, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = key.getBytes("UTF-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
        return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码
    }

    /**
     * AES 解密
     *
     * @param value
     * @param key
     * @param isDecodeURL
     * @return
     * @throws Exception
     */
    public static String AESDecrypt(String value, String key, boolean isDecodeURL) throws Exception {
        try {
            byte[] raw = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            if (isDecodeURL) {
                value = URLDecoder.decode(value, "UTF-8");
            }
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(value);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8");
            return originalString;
        } catch (Exception ex) {
            logger.error("AESDecrypt occur error.", ex);
            return null;
        }
    }


    /**
     * 将url参数转换成map
     *
     * @param "id=1&account=131&time="+time
     * @return
     */
    public static HashMap<String, Object> urlParamToMap(String param) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>(0);
        if (StringUtils.isBlank(param)) {
            return paramMap;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] split = params[i].split("=");
            if (split.length == 2) {
                paramMap.put(split[0], split[1]);
            }
        }
        return paramMap;
    }

    public static String commonInvoking(String url, String hostName, int port) throws Exception, IOException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(60000);
        requestFactory.setConnectTimeout(60000);
        requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostName, port)));
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<String> response = restTemplate.getForEntity(URLDecoder.decode(url, "UTF-8"), String.class);
        return response.getBody();
    }

    public static String httpGet(String url, String hostName, int port, String type) {
        logger.info("come httpGet url:{},hostName:{},port:{},type:{}", url, hostName, port, type);
        String result = null;
        //// 添加代理，IP为本地IP 8888就是fillder的端口
        HttpHost proxy = new HttpHost(hostName, port, "http");
        CloseableHttpClient httpClient = null;
        // 添加代理
        httpClient = HttpClients.createDefault();
        // 创建httpGet
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Connection", "keep-alive");

        // 代理
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        httpGet.setConfig(config);

        // 执行get请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            logger.info("come httpGet response:{}", response);
            int code = response.getStatusLine().getStatusCode();
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            logger.info("come httpGet entity:{}", entity);
            if (entity != null) {
                // 打印响应内容
                result = EntityUtils.toString(entity);
            }
            logger.info("httpGet type:{} \nurl:{} \nhostName:{} \nport:{} \n返回的状态码:{}/{} \nresponseInfo:{}", type, url, hostName, port, code, response.getStatusLine(), result);
        } catch (Exception e) {
            logger.error("httpGet type:{} \nurl:{} \nhostName:{} \nport:{} occur error.", type, url, hostName, port, e);
        } finally {
            closeHttpClientAndResponse(response, httpClient, url, null);
        }
        logger.info("come httpGet result:{}", result);
        return result;
    }

}