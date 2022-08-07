package com.indo.game.common.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

/**
 * 加密
 */
public class CMDAESEncrypt {
    static {
        //BouncyCastle是一个开源的加解密解决方案，主页在http://www.bouncycastle.org/
        Security.addProvider(new BouncyCastleProvider());
    }
    public static String encrypt(String data, String key){
        try {
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            // iv向量默认采用key反转
            String iv = new StringBuilder(key).reverse().toString();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            // Base64
            java.util.Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(encrypted);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Sample
    public static void main(String[] args) throws Exception {
        String encryptData = "{\"DateSent\":1659768273680,\"PackageId\":\"4d2deca9-d022-4157-8b00-1710c35dfa58\",\"DateReceived\":\"637953938687177578\",\"StatusCode\":100,\"Balance\":1000344.00,\"StatusMessage\":\"Success\"}"; // $ { KEY }
        String key = "4023461570052130";
        String encryptStr = encrypt(encryptData, key);
        System.out.println("加密    "+encryptStr);
        System.out.println("解密    "+CMDAESDecrypt.decrypt(encryptStr, key));
//        String data2 = "{\"SourceName\":\"USER\",\"TransactionAmount\":-100.0,\"ReferenceNo\":\"HDP12345678\",\"ActionId\":1003}";
//        System.out.println(encrypt(data2, key));
    }
}
