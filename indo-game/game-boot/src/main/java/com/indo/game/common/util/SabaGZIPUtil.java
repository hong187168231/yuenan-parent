package com.indo.game.common.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.zip.GZIPInputStream;

public class SabaGZIPUtil {

    /**

     * 解压 gzip 格式 byte 数组

     * @param bytes gzip 格式 byte 数组

     * @param charset 字符集

     */

    public static String uncompress(byte[] bytes, String charset) {
        System.out.println("111111==================4");
        if (bytes == null || bytes.length == 0) {

            return null;

        }
        System.out.println("111111==================5");
        ByteArrayOutputStream byteArrayOutputStream = null;

        ByteArrayInputStream byteArrayInputStream = null;

        GZIPInputStream gzipInputStream = null;

        try {
            System.out.println("111111==================6");
            byteArrayOutputStream = new ByteArrayOutputStream();

            byteArrayInputStream = new ByteArrayInputStream(bytes);

            gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            System.out.println("111111==================7");
// 使用 org.apache.commons.io.IOUtils 简化流的操作

            IOUtils.copy(gzipInputStream, byteArrayOutputStream);
            System.out.println("111111==================8");
            return byteArrayOutputStream.toString(charset);

        } catch (IOException e) {
            System.out.println("111111=====eeeeeeee=============5");
            e.printStackTrace();

        } finally {

// 释放流资源

            IOUtils.closeQuietly(gzipInputStream);

            IOUtils.closeQuietly(byteArrayInputStream);

            IOUtils.closeQuietly(byteArrayOutputStream);

        }

        return null;

    }
    public static JSONObject getJSONObject(HttpServletRequest request){
        String params = "";
        try {
            // 获取 Content-Encoding 请求头
            String contentEncoding = request.getHeader("content-encoding");
            System.out.println("00000=================="+contentEncoding);
            if(null!=contentEncoding && "gzip".equals(contentEncoding)){
                System.out.println("111111==================1");
                System.out.println(request.getReader());
                // 获取输入流
                BufferedReader reader = request.getReader();
                // 将输入流中的请求实体转换为 byte 数组, 进行 gzip 解压
                byte[] bytes = IOUtils.toByteArray(reader, "iso-8859-1");
                System.out.println("111111==================2");
                // 对 bytes 数组进行解压
                params = SabaGZIPUtil.uncompress(bytes, "utf-8");
                System.out.println("111111==================3");
            } else {
                BufferedReader reader = request.getReader();
                params = IOUtils.toString(reader);
                System.out.println("222222==================");
            }
            if (params != null && params.trim().length() > 0) {
                System.out.println("333333=================="+params);
                // 因为前台对参数进行了 url 编码, 在此进行解码
                params = URLDecoder.decode(params, "utf-8");
                System.out.println("66666=================="+params);
                // 将解码后的参数转换为 json 对象
                return JSONObject.parseObject(params);
            }
            System.out.println("44444==================");
        } catch (IOException e) {
            System.out.println("eeeee==================");
            e.printStackTrace();
        }
        return null;
    }
}
