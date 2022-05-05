package com.indo.game.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 解密
 */
public class UGDecrypt {
    public String decodeApiPassword(String apiPassword, String apiKey, String operatorId) throws Exception {

        // If number of characters is less than 16 digit, Leading 0 is filled until 16 digit number is satisfied.
        // Conversely, if number of characters exceeds 16 digit, take last 16 digital.
        int operatorIdLength = operatorId.length();
        if (operatorIdLength < 16) {
            operatorId = StringUtils.leftPad(operatorId, 16, '0');
        } else if (operatorIdLength > 16) {
            operatorId = StringUtils.substring(operatorId, operatorIdLength - 16, operatorIdLength);
        }

        // Must be converted to lowercase before use
        operatorId = operatorId.toLowerCase();

        // AES
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(apiKey.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(operatorId.getBytes(StandardCharsets.UTF_8)));

        // Base64
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(cipher.doFinal(decoder.decode(apiPassword)), StandardCharsets.UTF_8);
    }
}
