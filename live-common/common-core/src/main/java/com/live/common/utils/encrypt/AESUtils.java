package com.live.common.utils.encrypt;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

public class AESUtils {
    static final String KEY_ALGORITHM = "AES";
    static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";
    public static final String KEY = "TSR304fd4&2020";
    //private static Cipher cipher = null;

    public static ConcurrentHashMap<String, Object> encryptCipherMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Object> decryptCipherMap = new ConcurrentHashMap<>();

	/*static {
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}*/

    public static String encryptData(String content, String rawpassword) {
        byte[] contentBytes = null;
        try {
            contentBytes = encrypt(content.getBytes("utf-8"), checkKey(rawpassword).getBytes("utf-8"), rawpassword);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encode(contentBytes);
    }

    public static String checkKey(String rawpassword) {
        int strLen = rawpassword.length();
        if (strLen > 16) {
            rawpassword = rawpassword.substring(0, 16);
        } else {
            while (strLen < 16) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(rawpassword).append("0");
                rawpassword = buffer.toString();
                strLen = rawpassword.length();
            }
        }
        return rawpassword;
    }

    public static String decryptData(String cipherString, String rawpassword) {
        String result = "";
        byte[] cipherBytes = null;
        byte[] contentBytes = null;
        try {
            cipherBytes = Base64.decode(cipherString);
            contentBytes = decrypt(cipherBytes, checkKey(rawpassword).getBytes("utf-8"), rawpassword);
            if (contentBytes != null) {
                result = new String(contentBytes, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static byte[] encrypt(byte[] content, byte[] rawpassword) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(rawpassword, "AES");
			/*if (cipher != null) {
				cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			} else {
				cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
				cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			}*/
            Cipher cipher2 = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
            cipher2.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] result = cipher2.doFinal(content);
            return result;
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return null;
    }


    public static byte[] decrypt(byte[] content, byte[] rawpassword) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(rawpassword, "AES");
			/*if (cipher != null) {
				cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			} else {
				cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
				cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			}*/

            Cipher cipher2 = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
            cipher2.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] result = cipher2.doFinal(content);
            return result;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    /*private void init(String strKey) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());
        if (encryptCipherMap.get(strKey) == null) {
            Cipher encryptCipher = Cipher.getInstance("DES/ECB/NoPadding", "SunJCE");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            encryptCipherMap.put(strKey, encryptCipher);
        }

        if (decryptCipherMap.get(key) == null) {
            Cipher decryptCipher = Cipher.getInstance("DES/ECB/NoPadding", "SunJCE");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
            decryptCipherMap.put(strKey, decryptCipher);
        }
    }*/

    public static byte[] encrypt(byte[] content, byte[] rawpassword, String key) {
        try {
            //Security.addProvider(new com.sun.crypto.provider.SunJCE());
            if (encryptCipherMap.get(key) == null) {
                Cipher cipher2 = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
                SecretKeySpec secretKeySpec = new SecretKeySpec(rawpassword, "AES");
                cipher2.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                encryptCipherMap.put(key, cipher2);
            }

            byte[] result = ((Cipher) encryptCipherMap.get(key)).doFinal(content);
            return result;
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return null;
    }

    public static byte[] decrypt(byte[] content, byte[] rawpassword, String key) {
        try {
            //Security.addProvider(new com.sun.crypto.provider.SunJCE());
            if (decryptCipherMap.get(key) == null) {
                Cipher cipher2 = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
                SecretKeySpec secretKeySpec = new SecretKeySpec(rawpassword, "AES");
                cipher2.init(Cipher.DECRYPT_MODE, secretKeySpec);
                decryptCipherMap.put(key, cipher2);
            }

            byte[] result = ((Cipher) decryptCipherMap.get(key)).doFinal(content);
            return result;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    public static byte[] toBytes(String cipherString) {
        if (cipherString == null || "".equals(cipherString)) {
            return null;
        }
        int len = cipherString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(cipherString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] contentBytes) {
        String HEX = "0123456789ABCDEF";
        if (contentBytes == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(contentBytes.length * 2);
        for (int i = 0; i < contentBytes.length; i++) {
            result.append(HEX.charAt((contentBytes[i] >> 4) & 0x0f)).append(HEX.charAt(contentBytes[i] & 0x0f));
        }
        return result.toString();
    }

    private static byte[] getRawKey(byte[] rawpassword) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom sr = null;
            sr = SecureRandom.getInstance("SHA1PRNG");

            sr.setSeed(rawpassword);

            keygen.init(128, sr);

            SecretKey secretKey = keygen.generateKey();
            byte[] result = secretKey.getEncoded();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
