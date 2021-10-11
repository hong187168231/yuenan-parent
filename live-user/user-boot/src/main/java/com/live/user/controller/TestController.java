package com.live.user.controller;

import com.alibaba.fastjson.JSON;
import com.live.admin.api.SysParameterClient;
import com.live.admin.api.UserFeignClient;
import com.live.admin.pojo.entity.SysParameter;
import com.live.admin.pojo.entity.SysUser;
import com.live.common.annotation.AllowAccess;
import com.live.common.annotation.LoginUser;
import com.live.common.pojo.bo.LoginInfo;
import com.live.common.redis.utils.RedisUtils;
import com.live.common.result.Result;
import com.live.user.pojo.entity.MemBaseinfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    @Resource
    private UserFeignClient uerFeignClient;

    @ApiOperation(value = "hello")
    @GetMapping("/hello")
    public String detail(@LoginUser LoginInfo loginInfo) {
        MemBaseinfo memBaseInfo = new MemBaseinfo();
        memBaseInfo.setAccount("dd");
        memBaseInfo.setBalance(2000L);
        redisUtils.set("dsd", memBaseInfo);
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
    @ApiOperation(value = "hello3")
    @GetMapping("/hello3")
    public Object helloTwoT() {
        Result<SysUser> parameter = uerFeignClient.getUserByUsername("admin");
        return JSON.toJSONString(parameter);
    }


}
