package com.indo.user.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.req.mem.MemInfoReq;
import com.indo.user.pojo.req.mem.UpdateBaseInfoReq;
import com.indo.user.pojo.req.mem.UpdatePasswordReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.service.MemBaseInfoService;
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

    @ApiOperation(value = "个人基本信息", httpMethod = "POST")
    @PostMapping(value = "/info")
    @AllowAccess
    public Result<MemBaseInfoVo> info(@RequestBody MemInfoReq req) {
        return Result.success(memBaseInfoService.getMemBaseInfoByAccount(req.getAccount()));
    }

    @ApiOperation(value = "更改密码", httpMethod = "POST")
    @PostMapping(value = "/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordReq req, @LoginUser LoginInfo loginUser) {
        boolean flag = memBaseInfoService.updatePassword(req, loginUser);
        return Result.judge(flag);
    }

    @ApiOperation(value = "更新个人信息", httpMethod = "POST")
    @PostMapping(value = "/updateBaseInfo")
    public Result updateBaseInfo(@RequestBody UpdateBaseInfoReq req, @LoginUser LoginInfo loginUser) {
        memBaseInfoService.updateBaseInfo(req, loginUser);
        return Result.success();
    }

    @ApiOperation(value = "添加银行卡", httpMethod = "POST")
    @PostMapping(value = "/addbankCard")
    @AllowAccess
    public Result addbankCard(@RequestBody AddBankCardReq req) {
        memBaseInfoService.addbankCard(req);
        return Result.success();
    }
}
