package com.indo.game.common.util;

import org.apache.commons.net.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 解密
 */
public class AGDecrypt {

    public static String decryptDes(String decryptStr,String apiKey)throws Exception{
        byte[] sourceBytes = Base64.decodeBase64(decryptStr);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(apiKey.getBytes(StandardCharsets.UTF_8), "DES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decoded = cipher.doFinal(sourceBytes);
        return new String(decoded,StandardCharsets.UTF_8);
    }
}
