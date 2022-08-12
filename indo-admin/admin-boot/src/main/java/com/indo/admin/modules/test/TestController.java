package com.indo.admin.modules.test;

import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
@AllArgsConstructor
@Api(tags = "后台游戏管理接口")
public class TestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "游戏记录", httpMethod = "GET")
    @GetMapping(value = "/hello")
    public Result queryGame8Record() {

        String pass = passwordEncoder.encode("123456");
        return Result.success(pass);
    }


}
