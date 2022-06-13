package com.indo.common.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.indo.common.constant.SymbolConstant;
import com.indo.common.constant.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.StringTokenizer;

@Slf4j
public class IPAddressUtil {

    private static final char SEPARATOR = '_';

    private static final String UNKNOWN = "unknown";

    /**
     * 获取Ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if (index != -1) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }
        XFor = Xip;
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(XFor) ? "127.0.0.1" : XFor;
    }


    /**
     * 获取ip地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 返回Long类型的ip地址
     *
     * @return
     */
    public static Long ipToLong() {
        String ip = "192.168.1.201";
        StringTokenizer token = new StringTokenizer(ip, ".");
        Long result = 0L;
        result += Long.parseLong(token.nextToken()) << 24;
        result += Long.parseLong(token.nextToken()) << 16;
        result += Long.parseLong(token.nextToken()) << 8;
        result += Long.parseLong(token.nextToken());
        return result;
    }

    /**
     * 将数值型的ip地址转换为字符串类型
     *
     * @param ip 数值型的ip地址
     * @return
     */
    public static String LongToIpStr(Long ip) {
        StringBuilder sb = new StringBuilder();
        sb.append(ip >>> 24);
        sb.append(".");
        sb.append(String.valueOf((ip & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ip & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(ip & 0x000000FF));
        return sb.toString();
    }


    /**
     * 功能描述: 获取客户端所在地方
     *
     * @auther:
     * @param: []
     * @return: java.lang.String
     * @date: 2020/7/3 16:41
     */
    public static String getClientArea(HttpServletRequest request) {
        String area = null;
        try {
            String ip = getIpAddress(request);
            Map<String, String> map = null;
            if (map != null) {
                log.info("根据ip获取城市地址ipmap：{}", map);
                String country = map.get("country");
                String province = map.get("province");
                String city = map.get("city");

                if (org.apache.commons.lang3.StringUtils.isNotEmpty(province) || org.apache.commons.lang3.StringUtils.isNotEmpty(city)) {
                    if ("XX".equals(province) || "内网IP".equals(province)) {
                        province = SymbolConstant.BLANK;
                    }
                    if ("XX".equals(city) || "内网IP".equals(city)) {
                        city = SymbolConstant.BLANK;
                    }
                    area = province + city;
                }

            }
        } catch (Exception e) {
            log.error("获取域名出错", e);
        }
        return area;
    }

    /**
     *      *
     *      * getMobileDevice:(获取手机设备型号)
     *      * @param request
     *      * @return
     *     
     */
    public static String getMobileDevice(HttpServletRequest request) {
        String userAgentStr = request.getHeader("user-agent");
        log.info("-----userAgentStr----{}", userAgentStr);
        String deviceStr = null;
        try {
            UserAgent ua = UserAgentUtil.parse(userAgentStr);
            deviceStr = (null!=ua.getOs()&&!"".equals(ua.getOs()))?ua.getOs().toString():"";
        } catch (Exception e) {
            log.error("获取手机设备型号出错", e);
        }
        return deviceStr;
    }


    /**
     * 根据ip获取详细地址
     */
    public static String getCityInfo(String ip) {
        String api = String.format(GlobalConstants.Url.IP_URL, ip);
        JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
        return object.get("addr", String.class);
    }



}
