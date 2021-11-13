package com.indo.game.utils;

import com.google.common.base.Charsets;
import com.indo.common.utils.PatternUtil;
import com.indo.game.common.constant.Constants;
import com.indo.game.config.OpenAPIProperties;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.Key;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author
 */
public class AGUtil extends HttpCommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(AGUtil.class);

    private static final String hexDigIts[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    /**
     * MD5加密
     *
     * @param origin      字符
     * @param charsetName 编码
     * @return
     */
    public static String MD5Encode(String origin, String charsetName) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (null == charsetName || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception e) {
            logger.error("AGUtil MD5Encode occur error. origin:{}, charsetName:{}", origin, charsetName, e);
        }
        return resultString;
    }

    public static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }

    /**
     * des加密
     *
     * @param encryptString
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static String encryptDES(String encryptString, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(encryptKey), "DES"));
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));
        return Base64.encodeBase64String(encryptedData);
    }

    public static byte[] getKey(String keyRule) {
        Key key = null;
        byte[] keyByte = keyRule.getBytes();
        byte[] byteTemp = new byte[8];
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
            key = new SecretKeySpec(byteTemp, "DES");
        }
        return key.getEncoded();
    }

    /**
     * -校验ag余额查询接口返回
     *
     * @param resultInfo
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static BigDecimal checkResponseBalance(String resultInfo) throws ParserConfigurationException, SAXException, IOException {
        BigDecimal bigDecimal = null;
        logger.info("ag balance:" + resultInfo);
        if (PatternUtil.isNumber(resultInfo)) {
            bigDecimal = new BigDecimal(resultInfo);
        }
        logger.info("ag balance convert:{}", bigDecimal);
        return bigDecimal;
    }


    public static String randomUID() {
        int random = (int) (Math.random() * 9 + 1);
        String valueOf = String.valueOf(random);
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        String billNo = valueOf + String.format("%015d", hashCode);
        return billNo;
    }

    public static String buildBillNo(Integer uid) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return format.format(new Date()) + uid;
    }

    public static String commonInvoking(HashMap<String, String> hashMap, String apiURL, String hostName, int port) throws Exception, IOException {
        String params = AGUtil.encryptDES(hashMap.toString().replace("{", "").replace("}", "").replace(",", "/\\\\/"), OpenAPIProperties.AG_DES_KEY);
        String key = AGUtil.MD5Encode(params + OpenAPIProperties.AG_MD5_KEY, Charsets.UTF_8.name());
        String url = apiURL + params + "&key=" + key;
        if (apiURL.contains("forwardGame")) {// 进入游戏
            return url;
        }
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(60000);
        requestFactory.setConnectTimeout(60000);
        requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)));
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;
        // 设置AG代理编号
        headers.add("User-Agent", "WEB_LIB_GI_" + hashMap.get(Constants.AG_API_PARAM_CAGENT));
        ResponseEntity<String> response = client.exchange(url, method, new HttpEntity<>(headers), String.class);
        return response.getBody();
    }
}
