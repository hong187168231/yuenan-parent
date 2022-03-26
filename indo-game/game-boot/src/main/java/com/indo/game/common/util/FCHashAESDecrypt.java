package com.indo.game.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * FC电子请求参数HASH解密
 */
public class FCHashAESDecrypt {
    public static String decrypt(String data, String agentKey) throws Exception {

        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        SecretKeySpec keySpec = new SecretKeySpec(agentKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(cipher.doFinal(Base64.decodeBase64(data)));
    }

    // Sample
    public static void main(String[] args) throws Exception {
        String key = "SjnQIV3OlpqEvKiN"; // $ { KEY }
        String encryptData = "ZZApvVBOs6ZFlXeQpwmeIxE9YZ35zGCDbzMPUjkhG4f7f7QiGpCVfZETnR+3UmwR";
        System.out.println(decrypt(encryptData, key));
// decrypt data =>
//        {"action":47,"ts":1632385601439,"lang":"en","gType":"0","mType":"8001","windowMode":"2"}
    }
}
