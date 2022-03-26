package com.indo.game.common.util;

import com.indo.common.utils.encrypt.MD5;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * FC 电子请求参数hash 加密
 */
public class FCHashAESEncrypt {

    /**
     * 加密
     *
     * @param data
     * @param agentKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String agentKey) {
        System.out.println("FCHashAESEncrypt.encrypt: " + data);
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            SecretKeySpec keySpec = new SecretKeySpec(agentKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return encoder.encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptMd5(String data) {
        try {
            System.out.println(data);
            return MD5.md5(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String key = "SjnQIV3OlpqEvKiN"; // $ { KEY }
        // cf4e6c6b0e8c9dab74832204d5cef1ce
        System.out.println(encryptMd5("{\"MemberAccount\":\"test032301\",\"GameID\":\"21003\"}"));
        System.out.printf(encrypt("{\"MemberAccount\":\"test032301\",\"GameID\":\"21003\"}", key));
    }

}
