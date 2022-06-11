package com.indo.game.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

@Slf4j
public class SAJDecryption {

    public static String decrypt(String data, String agentKey){

        try{
            String decodedstr = java.net.URLDecoder.decode(data, "UTF-8");
            System.out.println("URLDecoded: " + decodedstr + agentKey);
            log.info("SAJDecryption.URLDecoded {}, {}, {}", data, decodedstr, agentKey);
            byte[] encryptKey = agentKey.getBytes(StandardCharsets.UTF_8);
            KeySpec keySpec = new DESKeySpec(encryptKey);
            SecretKey myDesKey = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            IvParameterSpec iv = new IvParameterSpec(encryptKey);

            Cipher desCipher;

            // Create the cipher
            desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            byte[] text = Base64.getDecoder().decode(decodedstr.getBytes());
            System.out.println("Text [Byte Format] : " + text);
            String t = Base64.getEncoder().encodeToString(text);

            // Initialize the same cipher for decryption
            desCipher.init(Cipher.DECRYPT_MODE, myDesKey, iv);


            //Decrypt the text
            byte[] textDecrypted = desCipher.doFinal(text);

//            System.out.println("Text Decryted : " + new String(textDecrypted));
            return new String(textDecrypted);
        }catch(Exception e){
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
