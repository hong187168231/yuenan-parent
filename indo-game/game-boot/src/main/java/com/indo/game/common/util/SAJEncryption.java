package com.indo.game.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

public class SAJEncryption {
    public static String encrypt(String data, String agentKey) {
        try {

            byte[] encryptKey = agentKey.getBytes(StandardCharsets.UTF_8); // encryptKey
            KeySpec keySpec = new DESKeySpec(encryptKey);
            SecretKey myDesKey = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            IvParameterSpec iv = new IvParameterSpec(encryptKey);

            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");

            Cipher desCipher;

            // Create the cipher
            desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            // Initialize the cipher for encryption
            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey, iv);

            //sensitive information
//            String source = "QS";
            byte[] text = data.getBytes();

            System.out.println("Text [Byte Format] : " + text);
            System.out.println("Text : " + new String(text));

            // Encrypt the text
            byte[] textEncrypted = desCipher.doFinal(text);
            String t = Base64.getEncoder().encodeToString(textEncrypted);

            System.out.println("Text Encryted [Byte Format] : " + textEncrypted);
            System.out.println("Text Encryted : " + t);
            return t;
//            // Initialize the same cipher for decryption
//            desCipher.init(Cipher.DECRYPT_MODE, myDesKey, iv);
//
//            // Decrypt the text
//            byte[] textDecrypted = desCipher.doFinal(textEncrypted);
//
//            System.out.println("Text Decryted : " + new String(textDecrypted));

//            return new String(textDecrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
