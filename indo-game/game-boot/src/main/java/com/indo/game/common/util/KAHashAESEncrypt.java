package com.indo.game.common.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * KA游戏 请求参数hash 加密
 */
public class KAHashAESEncrypt {

    /**
     * 加密
     * @param message message
     * @param secretKey secretKey
     * @return String
     * @throws Exception Exception
     */
    public static String encrypt(String message, String secretKey) {
        System.out.println(message);
        String hash = null;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            return hash;
        }
        System.out.println(hash);
        return hash;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
                stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    public static void main(String[] args) {
        String message = "{\"depositAmount\":1000,\"accessKey\":\"6B7E725FD47EC18168A5568F0867CA88\",\"partnerName\":\"KA\",\"currency\":\"USD\",\"username\":\"swuserid\"}";
        String message1 = "{\"accessKey\":\"6B7E725FD47EC18168A5568F0867CA88\",\"currency\":\"USD\",\"depositAmount\":1000,\"partnerName\":\"KA\",\"username\":\"swuserid\"}";
        System.out.println(encrypt(message, "34389D16CF495410CA6AD17FD9CB3750"));
        System.out.println(encrypt(message1, "34389D16CF495410CA6AD17FD9CB3750"));
String msg = "{\"transactionId\":\"76a5d8bcc406710d652a9cffa0397f34\",\"betAmount\":6000,\"winAmount\":0,\"amount\":10000,\"jpc\":0,\"selections\":5,\"betPerSelection\":300,\"freeGames\":false,\"round\":0,\"roundsRemaining\":0,\"complete\":true,\"timestamp\":1645501617408,\"sessionId\":\"df58bb81ceb748196d97f3414bc69b42\",\"playerId\":\"swuserid\",\"currency\":\"USD\",\"action\":\"play\",\"gameId\":\"SuperShot2\",\"playerIp\":\"0:0:0:0:0:0:0:1\",\"type\":\"BonusPick\",\"token\":\"bd7ac51e8f71398216d08addaa710061244296a8c5bd23b0c0a608e4ebdc0c70\",\"partnerPlayerId\":\"swuserid\"}";
        System.out.println(encrypt(msg, "34389D16CF495410CA6AD17FD9CB3750"));
    }
}
