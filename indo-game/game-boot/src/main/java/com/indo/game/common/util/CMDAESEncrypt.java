package com.indo.game.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密
 */
public class CMDAESEncrypt {
    public static String encrypt(String data, String key) {
        try {
            // iv向量默认采用key反转
            String iv = new StringBuilder(key).reverse().toString();
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes("UTF-8");
            int plainTextLength = dataBytes.length;
            if (plainTextLength % blockSize != 0) {
                plainTextLength = plainTextLength + (blockSize - plainTextLength % blockSize);
            }
            byte[] plaintext = new byte[plainTextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return Base64.encodeBase64URLSafeString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Sample
    public static void main(String[] args) throws Exception {
//        String encryptData = "{\"ActionId\":1000,\"SourceName\":\"USER\"}"; // $ { KEY }
//        String key = "0724955629056261";
//        System.out.println(encrypt(encryptData, key));
//        String data2 = "{\"SourceName\":\"USER\",\"TransactionAmount\":-100.0,\"ReferenceNo\":\"HDP12345678\",\"ActionId\":1003}";
//        System.out.println(encrypt(data2, key));
    }
}
