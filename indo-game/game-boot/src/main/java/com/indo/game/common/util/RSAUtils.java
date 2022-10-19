package com.indo.game.common.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {
    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;//设置长度
    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 生成公、私钥
     * 根据需要返回String或byte[]类型
     * @return
     */
    private static Map<String, String> createRSAKeys(){
        Map<String, String> keyPairMap = new HashMap<String, String>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            /*Map<String, byte[]> byteMap = new HashMap<String, byte[]>();
            byteMap.put(PUBLIC_KEY_NAME, publicKey.getEncoded());
            byteMap.put(PRIVATE_KEY_NAME, privateKey.getEncoded());*/

            //获取公、私钥值
            String publicKeyValue = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyValue = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            //存入
            keyPairMap.put(PUBLIC_KEY, publicKeyValue);
            keyPairMap.put(PRIVATE_KEY, privateKeyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyPairMap;
    }

    /**
     * 解码PublicKey
     * @param key
     * @return
     */
    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解码PrivateKey
     * @param key
     * @return
     */
    public static PrivateKey getPrivateKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 签名
     * @param key	私钥
     * @param requestData	请求参数
     * @return
     */
    public static String sign(String key, String requestData){
        String signature = null;
        byte[] signed = null;
        try {
            PrivateKey privateKey = getPrivateKey(key);

            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(requestData.getBytes());
            signed = Sign.sign();

            signature = Base64.getEncoder().encodeToString(signed);
            System.out.println("===签名结果："+signature);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signature;
    }

    /**
     * 验签
     * @param key	公钥
     * @param requestData	请求参数
     * @param signature	签名
     * @return
     */
    public static boolean verifySign(String key, String requestData, String signature){
        boolean verifySignSuccess = false;
        try {
            PublicKey publicKey = getPublicKey(key);

            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(requestData.getBytes());

            verifySignSuccess = verifySign.verify(Base64.getDecoder().decode(signature));
            System.out.println("===验签结果："+verifySignSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return verifySignSuccess;
    }

    public static void main(String[] args) {
//        Map<String, String> keyPairMap = createRSAKeys();
//        System.out.println("生成公、私钥测试："+keyPairMap);

//        String publicKey = keyPairMap.get(PUBLIC_KEY);
//        String privateKey = keyPairMap.get(PRIVATE_KEY);
        String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCJgsSc6XzSwy/SgGIPsVOMxbIGTyjqcpmUnXoFGcQOBdleBwKWhlauM+A2rMZgqm9G1goZvieEWzs376BPtZ647YFsRJBou6sxcUX603zPeMNo8CQT7ZDn+QDkS2RB1Wyow881rjppiMRUfEycdcKv4RYTsHPvlMkLoRcM26kNUmVH0vmK/Mhvw80YqsRMUzcDk7X1QrcV7fVcxE//8xyHZWY3tS/ycmpHD86c7vlzQ5x/h32imxYq8f0IujM0nD2jsLoJ2+4z6ZibXTpWxAO8a8kbWI7AZJ5TEv9fs2aUM/o3XZ9rhmc/WXjQNIwuI7h0tyYK2zKFymbS7OLTam9JAgMBAAECggEAJ7cM8gS9eGHHPhS2PbIJX275q7jRKGVQ1gtlYnO+nA40aWO865yBDG8PIrtj0CdpXBwVbrnsz91JjS44Ls1VujNeXdavnpAVMYJ1o6vRMFrAlV5GDTtfzp+sEkN0AU+SwmtLPUXEuRY3He+IcOfSAtoPdkBQO7HNHv63mY8XlDD7OI4P2NW7wCDnG2dt/nVoEopTzjKUE1iHw4jXsXQEUP0eRhg8in+Ri5E38+QMpEilqS84OO0SBTHaTQzGxJKGaFQvDUP/gXhguGApwHwMh3zD5cWVQ6/T8GFn7EWlkczDAynqhnWua3Z8gAZTTp8AYOKTOur4lOBOsz/48jTuwQKBgQDK/USY0UPvIxB0NuRthT16RZRgwgLdilHMydI9RnvgibpPih4DXl8ceQ838MCSCWPvLXR5MY/VRkNw+dqV+dXuwCjGjHR4hg0eULoQMntUjScnuJdBP+PbLAGFnVRpCMgERE/p5L7VvtUin1fC2uk6fJrtdl5gndlPXJu3bWhfVwKBgQCta/jy6vxNpJqUcQz5bhlT5chIGdgp9zTJMf2bWXFQQ8MebMyWyxfm3VRJkMCutmhOg1jlK4If+FJeMkRni12jkgWUQTQ3f6lgvwxORYfpx+uJVMb/Cke/Ccg4cCwcmviOFDRCZarwDYrv9mWBaetNtrfBmDDRCuDQOAMZ1zaiXwKBgBJ6h9oZgsOsP3002gqWTIhq+RztzHD6fIPWA4uMdQ51uz4uZpkL6EChTJ9dDUq+DBjj3GyD9IIHyW/pbtqkSbNYTt30P04RdfRCiQ2catnBl8Tn6u0Hsehr0x+kuOBu6jZpOZUjO7QdkS17r/OPFhg666eeVO0asYOl/I7wKMY1AoGAIMfcEu7gKj0HCzAjZ7KsDvPMW/Smq6gSzkUozwilJqroYwPl2EsjhXwoxo8aHOxV4yFo2EjVG/3wc+XEkF4DoTuk2lVI7YSwG52BUfpKqqIjn53qz6K91RqgQirR68ZPXWvsRPC60w18ScelgETpOZNkQK/n6lvICxPLYkMQDLkCgYB9mBew8CQ/BuLYDpEFXkwNF5zzGItlvogmh1X+EX2DClC/eV71cgwh/e4FRrTW5HOECsbduGJ9YgTujr+/zL7kxoOOoXzJZZacspgCrKXthNd8DViBevUHEIwfB2OnNsCTONIr3m80sQoyWAoGOqWhxsdOlfwlH7UXfe1IqK51ww==";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiYLEnOl80sMv0oBiD7FTjMWyBk8o6nKZlJ16BRnEDgXZXgcCloZWrjPgNqzGYKpvRtYKGb4nhFs7N++gT7WeuO2BbESQaLurMXFF+tN8z3jDaPAkE+2Q5/kA5EtkQdVsqMPPNa46aYjEVHxMnHXCr+EWE7Bz75TJC6EXDNupDVJlR9L5ivzIb8PNGKrETFM3A5O19UK3Fe31XMRP//Mch2VmN7Uv8nJqRw/OnO75c0Ocf4d9opsWKvH9CLozNJw9o7C6CdvuM+mYm106VsQDvGvJG1iOwGSeUxL/X7NmlDP6N12fa4ZnP1l40DSMLiO4dLcmCtsyhcpm0uzi02pvSQIDAQAB";
        System.out.println("===开始RSA公、私钥测试===");
        String str = "zfsvnzf168testswuserid11566360289094";
        String sign = sign(privateKey, str);

        verifySign(publicKey, str, sign);
    }

}