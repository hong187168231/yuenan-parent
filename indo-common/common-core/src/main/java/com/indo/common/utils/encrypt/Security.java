package com.indo.common.utils.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Qiang
 * @date 2017年7月27日 上午11:18:04
 */
public final class Security {

    private static final Logger logger = LoggerFactory.getLogger(Security.class);

    /**
     * 定义加密迭代次数
     */
    private static final int HASH_ITERATIONS = 3;

    private Security() {
    }

    /**
     * @param str  待加密字符串
     * @param salt 加密盐
     * @Author: admin
     * @Description: MD5 + 盐 迭代加密
     * @Version: 1.0.0
     * @Date; 2018/4/25 15:45
     * @return: java.lang.String
     */
    public final static String MD5SaltEncrypt(String str, String salt) {
        String md5 = str + salt;
        for (int i = 0; i < HASH_ITERATIONS; i++) {
            md5 = MD5(md5);
        }
        return md5;
    }

    /**
     * @param str 待加密字符串
     * @Author: admin
     * @Description: MD5 迭代加密
     * @Version: 1.0.0
     * @Date; 2018/4/25 15:45
     * @return: java.lang.String
     */
    public final static String MD5Encrypt(String str) {
        String md5 = str;
        for (int i = 0; i < HASH_ITERATIONS; i++) {
            md5 = MD5(md5);
        }
        return md5;
    }

    /**
     * MD5方法
     *
     * @param string
     * @return String
     */
    public final static String MD5(String string) {
        try {
            return byteArrayToHexString(MessageDigest.getInstance("MD5").digest(string.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5 encrypt occur error.", e);
        }
        return null;
    }

    /**
     * 哈希方法
     *
     * @param string
     * @return String
     */
    public final static String SHA(String string) {
        try {
            return byteArrayToHexString(MessageDigest.getInstance("SHA").digest(string.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            logger.error("SHA encrypt occur error.", e);
        }
        return null;
    }

    /**
     * byteArrayToHexString
     *
     * @param bytes
     * @return
     * @author ruan 2013-7-17
     */
    private final static String byteArrayToHexString(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }
}
