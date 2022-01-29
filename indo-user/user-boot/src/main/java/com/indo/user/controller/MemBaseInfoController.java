package com.indo.user.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.user.pojo.bo.MemTradingBO;
import com.indo.user.pojo.req.LogOutReq;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.req.mem.UpdateBaseInfoReq;
import com.indo.user.pojo.req.mem.UpdatePasswordReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.service.AppMemBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "会员接口")
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class MemBaseInfoController {

    @Resource
    private AppMemBaseInfoService memBaseInfoService;


    @ApiOperation(value = "账号检查", httpMethod = "GET")
    @GetMapping(value = "/checkAccount")
    @ApiImplicitParam(name = "account", value = "账号", required = true, paramType = "query", dataType = "string")
    @AllowAccess
    public Result check(@RequestParam("account") String account) {
        boolean flag = memBaseInfoService.checkAccount(account);
        return Result.success(flag);
    }

    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @PostMapping(value = "/login")
    @AllowAccess
    public Result<AppLoginVo> loginDo(@RequestBody LoginReq req) {
        return memBaseInfoService.appLogin(req);
    }

    @ApiOperation(value = "退出登录", httpMethod = "POST")
    @PostMapping(value = "/logout")
    public Result logout(@RequestBody LogOutReq req) {
        return Result.judge(memBaseInfoService.logout(req));
    }

    @ApiOperation(value = "注册接口", httpMethod = "POST")
    @PostMapping(value = "/register")
    @AllowAccess
    public Result<AppLoginVo> register(@RequestBody @Validated RegisterReq req) {
        return memBaseInfoService.register(req);
    }

    @ApiOperation(value = "个人基本信息", httpMethod = "POST")
    @PostMapping(value = "/info")
    public Result<MemBaseInfoVo> info(@LoginUser LoginInfo loginUser) {
        return Result.success(memBaseInfoService.getMemBaseInfo(loginUser.getAccount()));
    }

    @ApiOperation(value = "更改密码", httpMethod = "POST")
    @PostMapping(value = "/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordReq req, @LoginUser LoginInfo loginUser) {
        boolean flag = memBaseInfoService.updatePassword(req, loginUser);
        return Result.judge(flag);
    }

    @ApiOperation(value = "修改头像", httpMethod = "POST")
    @PostMapping(value = "/updateHeadImage")
    @ApiImplicitParam(name = "headImage", value = "头像地址", required = true, paramType = "query", dataType = "String")
    public Result updateHeadImage(@RequestParam("headImage") String headImage, @LoginUser LoginInfo loginUser) {
        boolean flag = memBaseInfoService.updateHeadImage(headImage, loginUser);
        return Result.judge(flag);
    }


    @ApiOperation(value = "更新个人信息", httpMethod = "POST")
    @PostMapping(value = "/updateBaseInfo")
    public Result updateBaseInfo(@RequestBody UpdateBaseInfoReq req, @LoginUser LoginInfo loginUser) {
        memBaseInfoService.updateBaseInfo(req, loginUser);
        return Result.success();
    }


    @ApiOperation(value = "用户资金信息", httpMethod = "GET")
    @GetMapping(value = "/tradingInfo")
    public Result<MemTradingBO> tradingInfo(@LoginUser LoginInfo loginUser) {
        MemTradingBO tradingV0 = memBaseInfoService.tradingInfo(loginUser.getAccount());
        return Result.success(tradingV0);
    }
}
