package com.indo.user.controller;

import com.indo.user.service.MemBaseInfoService;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.req.LoginReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "会员接口")
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@AllArgsConstructor
public class MemBaseInfoController {

    @Resource
    private MemBaseInfoService memBaseInfoService;

    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @PostMapping(value = "/login")
    @AllowAccess
    public Result<AppLoginVo> loginDo(@RequestBody LoginReq req) {
        return memBaseInfoService.appLogin(req);
    }

    @ApiOperation(value = "注册接口", httpMethod = "POST")
    @PostMapping(value = "/register")
    @AllowAccess
    public Result<AppLoginVo> register(@RequestBody RegisterReq req) {
        return memBaseInfoService.register(req);
    }


}
