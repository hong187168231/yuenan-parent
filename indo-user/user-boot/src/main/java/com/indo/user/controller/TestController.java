package com.indo.user.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.NoRepeatSubmit;
import com.indo.common.result.Result;
import com.indo.user.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试接口")
@RestController
@RequestMapping("/test")
@Slf4j
@AllArgsConstructor
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/hello")
    @ApiOperation(value = "hello", httpMethod = "GET")
    @AllowAccess
    @NoRepeatSubmit
    public Result hello(@RequestParam("name") String name) {
        return Result.success(testService.sayHello(name));
    }


}
