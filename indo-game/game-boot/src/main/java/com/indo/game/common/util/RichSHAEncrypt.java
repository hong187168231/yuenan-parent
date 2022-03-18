package com.indo.game.common.util;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA 256 hash加密
 */
public class RichSHAEncrypt {
    public static String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

    public static void main(String[] args) {
        String aa = getSHA256Str("ac_PFA1qwer12345abc1605671558");
        System.out.println(aa);
        System.out.println("be2ce840fad63d8c554aac7bc01bac5a0a3fa99669a60c3b4f374e5982f716e3".equals(aa));
    }
}
