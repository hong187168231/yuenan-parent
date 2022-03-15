package com.indo.game.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * PP电子请求参数HASH解密
 */
public class PPHashAESDecrypt {
    public static String decrypt(String data, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"),
                new IvParameterSpec(iv.getBytes()));
        String decryptData = new String(cipher.doFinal(Base64.decodeBase64(data)));
        return decryptData;
    }

    // Sample
    public static void main(String[] args) throws Exception {
        String key = "key1234567Sample"; // $ { KEY }
        String iv = "iv12345678Sample"; // $ { IV }
        String encryptData =
                "JYUwK_t1athMnX5mbEQ_stdBbYYgfIeC7utswa5A3Dw4vuORvpkTDWfmkFqpPtOPr_PJJA2WJLD4dBZV0qCnWAmBB7Fpuy7Rkgpbs-Xez6WlpKCApE4uY2TC0QegtWnj";
        System.out.println(decrypt(encryptData, key, iv));
// decrypt data =>
//        {"action":47,"ts":1632385601439,"lang":"en","gType":"0","mType":"8001","windowMode":"2"}
    }
}
