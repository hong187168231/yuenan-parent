package com.indo.game.common.util;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密
 */
public class TCGWinSHA256Encrypt {
    public static String encryptDes(String encryptString) throws Exception {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(encryptString.getBytes(StandardCharsets.UTF_8));
            encdeStr = Hex.encodeHexString(hash);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (UnsupportedOperationException e){
            e.printStackTrace();
        }
        return encdeStr;
    }

    // Sample
    public static void main(String[] args) throws Exception {
//        String data2 = "{\"SourceName\":\"USER\",\"TransactionAmount\":-100.0,\"ReferenceNo\":\"HDP12345678\",\"ActionId\":1003}";
//        System.out.println(encrypt(data2, key));
    }
}
