package com.indo.game.common.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * PP 电子请求参数hash 加密
 */
public class PPHashAESEncrypt {

    /**
     * 加密
     * 1.按字母表顺序对所有参数排序。
     * 2.在key1=value1&key2=value2 中附加它们。
     * 3.附加密钥，即：key1=value1&key2=value2SECRET。
     * 4.使用MD5 计算哈希。
     * @param data data
     * @param secretKey secretKey
     * @return String
     */
    public static String encrypt(Object data, String secretKey) {
        try{
            byte[] dataBytes = getOriginalHash(data, secretKey).getBytes(StandardCharsets.UTF_8);
            return DigestUtils.md5DigestAsHex(dataBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 排序获取对象除hash外的值用来生成hash
     * @param o Object
     * @param secretKey secretKey
     * @return String
     */
    private static String getOriginalHash(Object o, String secretKey) throws Exception {
        System.out.println("getOriginalHash: "+JSONObject.toJSONString(o));
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length-1];
        int strIdx = 0;
        for (int i = 0; i < fields.length; i++) {
            if ("hash".equals(fields[i].getName())) {
                continue;
            }
            fieldNames[strIdx] = fields[i].getName();
            strIdx ++;
        }
        Arrays.sort(fieldNames);
        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            Object fieldVal = getFieldValueByName(fieldName, o);
            if (null != fieldVal) {
                hash.append(fieldName).append("=").append(fieldVal);
                if(i < fieldNames.length - 1){
                    hash.append("&");
                }

            }
        }

        hash.append(secretKey);
        return hash.toString();
    }

    /**
     *  根据属性名获取属性值
     * @param fieldName fieldName
     * @param o Object
     * @return Object
     */
    private static Object getFieldValueByName(String fieldName, Object o) throws Exception {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = o.getClass().getMethod(getter, new Class[] {});
        return method.invoke(o, new Object[] {});
    }

}
