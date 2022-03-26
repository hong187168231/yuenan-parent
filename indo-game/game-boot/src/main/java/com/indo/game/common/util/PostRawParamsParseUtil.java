package com.indo.game.common.util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 获取POST请求RAW text格式（单个字符串）参数值
 */
public class PostRawParamsParseUtil {
    public static String getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i, contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }

        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }
}
