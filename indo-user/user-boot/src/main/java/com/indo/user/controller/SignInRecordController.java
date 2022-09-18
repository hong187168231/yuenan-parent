package com.indo.user.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.core.pojo.dto.LoanRecordDTO;
import com.indo.core.pojo.dto.SignInRecordDTO;
import com.indo.core.pojo.entity.LoanRecord;
import com.indo.core.pojo.entity.SignInRecord;
import com.indo.core.service.ISignInRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 签到相关 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-07-29
 */
@Api(tags = "签到相关")
@RestController
@RequestMapping("/api/v1/signInRecord")
public class SignInRecordController {
    @Resource
    private ISignInRecordService signInRecordServicel;

    @ApiOperation(value = "查询用户签到记录分页", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long")
    })
    @GetMapping(value = "/findMemSignInRecordPage")
    public Result<Page<SignInRecord>> findMemSignInRecordPage(SignInRecordDTO signInRecordDTO, LoginInfo loginInfo) {
        return Result.success(signInRecordServicel.findMemSignInRecordPage(signInRecordDTO,loginInfo));
    }

    @ApiOperation(value = "查询用户签到次数", httpMethod = "GET")
    @GetMapping(value = "/findUserSignInNum")
    public Result findUserSignInNum(@LoginUser LoginInfo loginInfo) {
        return Result.success(signInRecordServicel.findUserSignInNum(loginInfo));
    }

    @ApiOperation(value = "用户签到", httpMethod = "GET")
    @GetMapping(value = "/signIn")
    public Result signIn(@LoginUser LoginInfo loginInfo, HttpServletRequest request) {
        signInRecordServicel.signIn(loginInfo,request);
        return Result.success();
    }
}
