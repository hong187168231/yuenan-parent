package com.indo.game.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * T9 解密
 */
public class T9AESDecrypt {

    public static String decrypt(String data, String key, String ivValue) throws Exception {
        if (key == null) {
            return null;
        }
        if (key.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"),
                new IvParameterSpec(ivValue.getBytes()));
        String decryptData = new String(cipher.doFinal(Base64.decodeBase64(data)));
        return decryptData;
    }

    // Sample
    public static void main(String[] args) throws Exception {
        String key = "0123456789abcdef"; // $ { KEY }
        String iv = "abcdef0123456789"; // $ { IV }
        String zz = "FKgMpX13N9XpRt89ZknMOa_T_u_LkqWO2bzTuiXz8QE";
        System.out.println(decrypt(zz, key, iv));
    }

}
