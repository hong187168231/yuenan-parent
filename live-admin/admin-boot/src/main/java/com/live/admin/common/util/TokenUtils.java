package com.live.admin.common.util;

import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import com.live.common.constant.AuthConstants;
import com.live.common.pojo.bo.JwtTokenPayload;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;

/**
 * @author 
 * @date 2021-03-10
 */
public class TokenUtils {


    /**
     * 获取JWT的载体
     * @param token
     * @return
     */
    @SneakyThrows
    public static JwtTokenPayload getJWTPayload(String token) {
        token = token.replace(AuthConstants.AUTHORIZATION_PREFIX, Strings.EMPTY);
        JWSObject jwsObject = JWSObject.parse(token);
        JwtTokenPayload payload = JSONUtil.toBean(jwsObject.getPayload().toString(), JwtTokenPayload.class);
        return payload;
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    public static boolean isExpired(String token) {
        JwtTokenPayload payload = getJWTPayload(token);
        // 计算是否过期
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        Long exp = payload.getExp();
        if (exp < currentTimeSeconds) { // token已过期，无需加入黑名单
            return true;
        }
        return false;
    }





}
