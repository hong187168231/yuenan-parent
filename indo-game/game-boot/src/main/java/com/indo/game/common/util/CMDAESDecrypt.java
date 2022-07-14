package com.indo.game.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

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

    // Sample
    public static void main(String[] args) throws Exception {
//        String encryptData = "jI11wfffPw1nlNQ9aTbeW1yrPHEWLgbkG0DR6jiEZ0s="; // $ { KEY }
        String encryptData = "jI11wfffPw1nlNQ9aTbeW1yrPHEWLgbkG0DR6jiEZ0s=";
//        1401f25b871a838c05325e44329c63a0
        String key = "54C763B17593B7EA";
        System.out.println(decrypt(encryptData, key));
//        String data2 = "z8sPTyu+9jwib6H7x3TVIbDrSdSsz6hdx1WyBwDckOwR6uxxgxFCyYPfx8Pq4Rz4+z6Gtr1Y6deQ0TM30uT4EAX4IK J+fZYdvgsMWNZBWzL2So8MAvKqJP6X4fefw1A9";
//        System.out.println(decrypt(data2, key));
//        String xx = "9CuAok5ayWCJP2kFHKZp6rPjTmUD0jA3Y0JrsEI9_WiW9Ke32MZgDZ1GTbRyGly_";
//        System.out.println(decrypt(xx, key));
    }
}
