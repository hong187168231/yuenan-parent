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

        if (bytes == null || bytes.length == 0) {

            return null;

        }

        ByteArrayOutputStream byteArrayOutputStream = null;

        ByteArrayInputStream byteArrayInputStream = null;

        GZIPInputStream gzipInputStream = null;

        try {

            byteArrayOutputStream = new ByteArrayOutputStream();

            byteArrayInputStream = new ByteArrayInputStream(bytes);

            gzipInputStream = new GZIPInputStream(byteArrayInputStream);

// 使用 org.apache.commons.io.IOUtils 简化流的操作

            IOUtils.copy(gzipInputStream, byteArrayOutputStream);

            return byteArrayOutputStream.toString(charset);

        } catch (IOException e) {

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
            String contentEncoding = request.getHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.equals("gzip")) {
                // 获取输入流
                BufferedReader reader = request.getReader();
                // 将输入流中的请求实体转换为 byte 数组, 进行 gzip 解压
                byte[] bytes = IOUtils.toByteArray(reader, "iso-8859-1");
                // 对 bytes 数组进行解压
                params = SabaGZIPUtil.uncompress(bytes, "utf-8");
            } else {
                BufferedReader reader = request.getReader();
                params = IOUtils.toString(reader);
            }
            if (params != null && params.trim().length() > 0) {
                // 因为前台对参数进行了 url 编码, 在此进行解码
                params = URLDecoder.decode(params, "utf-8");
                // 将解码后的参数转换为 json 对象
                return JSONObject.parseObject(params);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
