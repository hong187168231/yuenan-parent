package com.indo.game.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 加密
 */
public class AGEncrypt {
    public static String encryptDes(String encryptString, String apiKey) throws Exception {
        // AES
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(apiKey.getBytes(StandardCharsets.UTF_8), "DES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        // Base64
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(cipher.doFinal(decoder.decode(encryptString)), StandardCharsets.UTF_8);
    }

    // Sample
    public static void main(String[] args) throws Exception {
//        String data2 = "{\"SourceName\":\"USER\",\"TransactionAmount\":-100.0,\"ReferenceNo\":\"HDP12345678\",\"ActionId\":1003}";
//        System.out.println(encrypt(data2, key));
    }
}
