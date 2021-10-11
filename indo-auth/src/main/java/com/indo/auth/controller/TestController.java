package com.indo.auth.controller;

import cn.hutool.core.convert.Convert;
import com.indo.auth.domain.MemberInfo;
import com.indo.auth.domain.OAuthToken;
import com.indo.auth.common.jwt.JwtGenerator;
import com.indo.common.constant.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Resource
    private JwtGenerator jwtGenerator;

    @GetMapping("/test")
    public Object postAccessToken() {
        // 自定义JWT生成
        // 1. JWT授权，一般存放用户的角色标识，用于资源服务器（网关）鉴权
        Set<String> authorities = new HashSet<>();
        // 2. JWT增强，携带用户ID等信息
        Map<String, String> additional = new HashMap<>();

        MemberInfo memberInfo = new MemberInfo();
        additional.put(AuthConstants.USER_ID_KEY, Convert.toStr(memberInfo.getAccount()));

        String accessToken = jwtGenerator.createAccessToken(authorities, additional);

        OAuthToken token = new OAuthToken().accessToken(accessToken);

        return "1";
    }

}
