package com.indo.user.controller;


import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.core.service.ISysTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 任务表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
@Api(tags = "任务相关")
@RestController
@RequestMapping("/api/v1/sysTask")
public class SysTaskController {
    @Resource
    private ISysTaskService sysTaskService;

    @ApiOperation(value = "查询任务信息及用户完成情况", httpMethod = "GET")
    @GetMapping(value = "/findMemTaskInfo")
    public Result findMemTaskInfo(@LoginUser LoginInfo loginInfo) {
        return Result.success(sysTaskService.findMemTaskInfo(loginInfo));
    }
    @ApiOperation(value = "领取任务奖励", httpMethod = "GET")
    @GetMapping(value = "/receiveTaskReward")
    @ApiImplicitParam(name = "taskId", value = "任务id", required = true,dataType = "int")
    public Result receiveTaskReward(@RequestParam Integer taskId, @LoginUser LoginInfo loginInfo, HttpServletRequest request) {
        sysTaskService.receiveTaskReward(loginInfo,taskId,request);
        return Result.success();
    }
}
