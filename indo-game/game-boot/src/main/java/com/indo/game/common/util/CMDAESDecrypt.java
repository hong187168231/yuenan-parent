package com.indo.game.common.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

/**
 * CMD请求参数HASH解密
 */
public class CMDAESDecrypt {
    /**
     * 解密
     * @param data data
     * @param key  key
     * @return
     */
    public static String decrypt(String data, String key) {
        try {
            // iv向量默认采用key反转
            String ivValue = new StringBuilder(key).reverse().toString();
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"),
                    new IvParameterSpec(ivValue.getBytes()));
            String decryptData = new String(cipher.doFinal(Base64.decodeBase64(data)));
            return decryptData;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 加密
     */
//    public static String encrypt(String data, String key) {
//        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//        int blockSize = cipher.getBlockSize();
//        byte[] dataBytes = data.getBytes("UTF-8");
//        int plainTextLength = dataBytes.length;
//        if (plainTextLength % blockSize != 0) {
//            plainTextLength = plainTextLength + (blockSize - plainTextLength % blockSize);
//        }
//        byte[] plaintext = new byte[plainTextLength];
//        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
//        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
//        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
//        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
//        byte[] encrypted = cipher.doFinal(plaintext);
//        return Base64.encodeBase64URLSafeString(encrypted);
//    }

    // Sample
    public static void main(String[] args) throws Exception {
//        String encryptData = "jI11wfffPw1nlNQ9aTbeW1yrPHEWLgbkG0DR6jiEZ0s="; // $ { KEY }
        String encryptData = "mXJvA3INZxyh/smR7+LWpSRZL2jQMdMpzsUUOSoDs08whaZB+erSG30EGmm399CI";
//        1401f25b871a838c05325e44329c63a0
        String key = "4023461570052130";
        String decryptStr = decrypt(encryptData, key);
        System.out.println("解密    "+decryptStr);
        // iv向量默认采用key反转
        String ivValue = new StringBuilder(key).reverse().toString();
        String encryptStr = JDBAESEncrypt.encrypt(encryptData, key,ivValue);
        System.out.println("加密    "+encryptStr);
        System.out.println("解密2    "+decrypt(encryptStr, key));
    }
}
