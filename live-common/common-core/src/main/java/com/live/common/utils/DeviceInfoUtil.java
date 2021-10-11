package com.live.common.utils;

import java.util.HashMap;

/**
 * @ClassName : DeviceInfoUtil
 * @Description : 获取设备信息
 * @Author :
 * @Date: 2020-11-15 21:52
 */
public class DeviceInfoUtil {
    private static final ThreadLocal<HashMap<String,Object>> infoHolder = new ThreadLocal<>();

    public static void setIp(String ip) {
        HashMap<String,Object> map =  infoHolder.get();
        if (map == null){
            map = new HashMap<String,Object>();
        }
        map.put("ip",ip);
        infoHolder.set(map);
    }

    /**
     * @description: 获取ip
     * @param
     * @return: java.lang.String
     * @author:
     * @time: 2020/11/15 22:00
    */
    public static String getIp() {
        HashMap<String,Object> map = infoHolder.get();
        if (map == null){
            return null;
        }
        return (String)map.get("ip");
    }


    public static void setArea(String area) {
        HashMap<String,Object> map =  infoHolder.get();
        if (map == null){
            map = new HashMap<>();
        }
        map.put("area",area);
        infoHolder.set(map);
    }

    /**
     * @description: 获取所在区域
     * @param
     * @return: java.lang.String
     * @author:
     * @time: 2020/11/15 22:00
    */
    public static String getArea() {
        HashMap<String,Object> map =  infoHolder.get();
        if (map == null){
            return null;
        }
        return (String)map.get("area");
    }


    public static void setType(String deviceName) {
        HashMap<String,Object> map =  infoHolder.get();
        if (map == null){
            map = new HashMap<>();
        }
        map.put("type",deviceName);
        infoHolder.set(map);
    }


    /**
     * @description: 获取设备类型
     * @param
     * @return: java.lang.String
     * @author:
     * @time: 2020/11/15 22:00
    */
    public static String getType() {
        HashMap<String,Object> map =  infoHolder.get();
        if (map == null){
            return null;
        }
        return (String)map.get("type");
    }

    /**
     * @description: 请求来源 来源  0苹果 1安卓 2web
     * @param source
     * @return: void
     * @author:
     * @time: 2020/11/16 10:31
    */
    public static void setSource(String source) {
        HashMap<String,Object> map =  infoHolder.get();
        if (map == null){
            map = new HashMap<>();
        }
        map.put("source",source);
        infoHolder.set(map);
    }

    public static String getSource() {
        HashMap<String,Object> map =  infoHolder.get();
        if (map == null){
            return null;
        }
        return (String)map.get("source");
    }

    /**
     * @description: 获取设备id
     * @param deviceId
     * @return: void
     * @author:
     * @time: 2020/11/16 10:50
    */
    public static void setDeviceId(String deviceId){
        HashMap<String,Object> map =  infoHolder.get();
        if (map == null){
            map = new HashMap<>();
        }
        map.put("deviceId",deviceId);
        infoHolder.set(map);
    }

    public static String getDeviceId() {
        HashMap<String,Object> map =  infoHolder.get();
        if (map == null){
            return null;
        }
        return (String)map.get("deviceId");
    }


    public static void remove() {
        infoHolder.remove();
    }
}
