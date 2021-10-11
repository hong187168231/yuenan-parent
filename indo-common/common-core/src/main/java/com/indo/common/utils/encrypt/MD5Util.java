package com.indo.common.utils.encrypt;

import org.springframework.util.LinkedMultiValueMap;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

public class MD5Util {

	public static Map<String, String> sign(Map params, String key) {
		Map<String, String> hashMap = new HashMap<>();
		// 字典序排序参数（小->大）
        SortedSet<String> keys = new TreeSet<>(params.keySet());
        StringBuffer buffer = new StringBuffer();
        for (String paramkey : keys) {
		    List<String> values = ((LinkedMultiValueMap<String, String>) params).get(paramkey);
		    for (String value : values) {
	            buffer.append(paramkey + "=" + value + "&");
		    }
		}
        StringBuffer md5Buffer = new StringBuffer();
        try {
			buffer.append("SecretKey=" + URLEncoder.encode(key, "UTF-8"));
			// 追加KEY
	        String sortParams = buffer.toString();
	        hashMap.put("params", sortParams);
	        // 加密并将转换为大写HEX
	        MessageDigest messageDigest =  MessageDigest.getInstance("MD5");
	        byte[] digest = messageDigest.digest(sortParams.getBytes());
	        for (byte b : digest) {
	            String hex = String.format("%02X", b & 0xff);
	            md5Buffer.append(hex);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        hashMap.put("sign", md5Buffer.toString());
        return hashMap;
    }
}
