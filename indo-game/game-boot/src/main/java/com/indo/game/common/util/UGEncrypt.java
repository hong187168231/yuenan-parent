package com.indo.game.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 加密
 */
public class UGEncrypt {
    public String produceApiPassword(String apiKey, String operatorId, long timestamp) throws Exception {

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

        // MD5
        String md5Data = (apiKey + operatorId).toLowerCase();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(md5Data.getBytes());
        String md5Hex = new BigInteger(1, md.digest()).toString(16);

        // Milliseconds No included
        if (String.valueOf(timestamp).length() == 13) {
            timestamp /= 1000;
        }

        String plainText = md5Hex + timestamp;

        // AES
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec keySpec = new SecretKeySpec(apiKey.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec,
                new IvParameterSpec(operatorId.getBytes(StandardCharsets.UTF_8)));

        // Base64
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
    }
}
