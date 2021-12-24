package com.indo.user.controller;

import com.alibaba.fastjson.JSON;
import com.indo.admin.api.SysParameterClient;
import com.indo.admin.api.UserFeignClient;
import com.indo.admin.pojo.entity.SysParameter;
import com.indo.admin.pojo.entity.SysUser;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.TestDTO;
import com.indo.user.pojo.entity.MemBaseinfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Api(tags = "会员接口")
@RestController
@RequestMapping("/test")
@Slf4j
@AllArgsConstructor
public class TestController {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private SysParameterClient sysParameterClient;
//    @Resource
//    private UserFeignClient uerFeignClient;

    @ApiOperation(value = "hello")
    @GetMapping("/hello")
    @AllowAccess
    public String detail(@LoginUser LoginInfo loginInfo) {
        MemBaseinfo memBaseInfo = new MemBaseinfo();
        memBaseInfo.setAccountNo("dd");
        memBaseInfo.setBalance(new BigDecimal(2000));
        redisUtils.set("dsd", memBaseInfo, 60 * 60 * 24 * 7);
        return "ok";
    }


    @AllowAccess
    @ApiOperation(value = "hello2")
    @GetMapping("/hello2")
    public Object helloTwo() {
        Result<SysParameter> parameter = sysParameterClient.getByParamCode("test11");
        return JSON.toJSONString(parameter);
    }


    @AllowAccess
    @PostMapping("/testValidated")
    public Result Validated(@Validated @RequestBody TestDTO testDTO) {
        return Result.success(testDTO);
    }


//    @AllowAccess
//    @ApiOperation(value = "hello3")
//    @GetMapping("/hello3")
//    public Object helloTwoT() {
//        Result<SysUser> parameter = uerFeignClient.getUserByUsername("admin");
//        return JSON.toJSONString(parameter);
//    }


}
