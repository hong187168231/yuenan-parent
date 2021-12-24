package com.indo.user.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.VerifyCodeReq;
import com.indo.user.service.sms.ISmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "短信验证码")
@RestController
@RequestMapping("/api/v1/sms")
@Slf4j
public class SmsController {

    @Autowired
    private ISmsService iSmsCodeService;

    @ApiOperation(value = "发送短信验证码", httpMethod = "POST")
    @PostMapping(value = "/send")
    @AllowAccess
    public Result send(@RequestBody VerifyCodeReq req, HttpServletRequest request) {
        return Result.success(iSmsCodeService.sendSmsCode(req, request));

    }


}
