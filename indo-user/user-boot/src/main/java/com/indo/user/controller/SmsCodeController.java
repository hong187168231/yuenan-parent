package com.indo.user.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.user.pojo.vo.sms.SmsCodeVo;
import com.indo.user.service.sms.ISmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "短信代码")
@RestController
@RequestMapping("/api/v1/smsCode")
@Slf4j
public class SmsCodeController {

    @Autowired
    private ISmsService iSmsCodeService;

    @ApiOperation(value = "可支持短信代码接口", httpMethod = "GET")
    @GetMapping(value = "/usable")
    @AllowAccess
    public Result<List<SmsCodeVo>> smsList() {
        return Result.success(iSmsCodeService.smsList());

    }


}
