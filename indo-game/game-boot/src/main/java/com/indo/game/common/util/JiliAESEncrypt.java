package com.indo.game.common.util;

import com.indo.common.utils.encrypt.MD5;
import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * jili游戏 请求参数hash 加密
 */
public class JiliAESEncrypt {

    /**
     * 加密{6 个任意字符} + MD5(所有请求参数串 + KeyG) + {6 个任意字符}
     *
     * @param obj     请求参数串 test0321002&GameId=1&Lang=zh-CN&AgentId=ZF168_JK
     * @param agentId secretKey
     * @return String
     * @throws Exception Exception
     */
    public static String encrypt(Object obj, String agentId, String agentKey) {

        String hash = null;
        try {
            String keyG = getKeyG(agentId, agentKey);
            hash = getSixRandom() + MD5.md5(obj + keyG) + getSixRandom();
        } catch (Exception e) {
            return hash;
        }
        System.out.println(hash);
        return hash;
    }

    /**
     * get key g
     * MD5(now + agentId + agentKey)
     *
     * @param agentId  agentId
     * @param agentKey agentKey
     * @return String
     */
    private static String getKeyG(String agentId, String agentKey) {
        String keyGBefore = getUTC_4TimeStr() + agentId + agentKey;
        System.out.println("keyGBefore: " + keyGBefore);
        String keyG = MD5.md5(keyGBefore);
        System.out.println("keyG: " + keyG);
        return keyG;
    }

    /**
     * @param o
     * @return
     * @desc 对象的值用来生成hash
     */
    private static String getOriginalHash(Object o, String agentId, String getKeyG) throws Exception {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }

        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            Object fieldVal = getFieldValueByName(fieldName, o);
            if (null != fieldVal) {
                hash.append(fieldName).append("=").append(fieldVal);
                if (i < fieldNames.length - 1) {
                    hash.append("&");
                }

            }
        }
        hash.append("AgentId=").append(agentId).append(getKeyG);
        System.out.println(hash);
        return hash.toString();
    }

    /**
     * @param fieldName
     * @param o
     * @return
     * @desc 根据属性名获取属性值
     */
    private static Object getFieldValueByName(String fieldName, Object o) throws Exception {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = o.getClass().getMethod(getter, new Class[]{});
        Object value = method.invoke(o, new Object[]{});
        return value;
    }

    /**
     * 生成6位随机字符
     *
     * @return
     */
    private static String getSixRandom() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    /**
     * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm"<br />
     * 如果获取失败，返回null
     *
     * @return
     */
    public static String getUTC_4TimeStr() {
        LocalDateTime utcNow = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime utc4Time = utcNow.minusHours(4);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMd");
        return utc4Time.format(formatter);
    }


    public static void main(String[] args) throws Exception {
//        String xx = "Token=Test006&GameId=1&Lang=zh-CN&AgentId=Jili_RMBbc7bb88a8cfe89d5053bc7e8fb54d390";
//        String yy = "Token=test0321002&GameId=1&Lang=zh-CN&AgentId=ZF168_JKcfa5dc9c42b5810fc1c20c8714a72178ffa11369";
//        System.out.println(MD5.md5(xx));
//        System.out.println(MD5.md5(yy));
        System.out.println(getUTC_4TimeStr());
        //getAvailableIDs
    }
}
