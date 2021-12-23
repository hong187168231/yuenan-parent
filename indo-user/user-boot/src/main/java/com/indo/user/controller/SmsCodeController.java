package com.indo.user.controller;

import com.indo.admin.api.MsgFeignClient;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.entity.MsgPushRecord;
import com.indo.admin.pojo.entity.MsgStationLetter;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import com.indo.user.pojo.vo.sms.SmsCodeVo;
import com.indo.user.service.ISmsCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "短信代码")
@RestController
@RequestMapping("/api/v1/smsCode")
@Slf4j
public class SmsCodeController {

    @Autowired
    private ISmsCodeService iSmsCodeService;

    @ApiOperation(value = "可支持短信代码接口", httpMethod = "GET")
    @GetMapping(value = "/usable")
    @AllowAccess
    public Result<List<SmsCodeVo>> smsList() {
        return Result.success(iSmsCodeService.smsList());

    }


}
