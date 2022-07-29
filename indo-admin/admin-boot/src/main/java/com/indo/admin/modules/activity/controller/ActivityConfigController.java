package com.indo.admin.modules.activity.controller;


import com.indo.admin.modules.activity.service.IActivityConfigService;
import com.indo.core.pojo.entity.ActivityConfig;
import com.indo.common.result.Result;
import com.indo.common.web.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 活动配置表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-07-13
 */
@Api(tags = "活动配置")
@RestController
@RequestMapping("/api/v1/activityConfig")
public class ActivityConfigController {
    @Resource
    private IActivityConfigService activityConfigService;

    @ApiOperation(value = "修改")
    @PutMapping(value = "/update")
    public Result update(@RequestBody ActivityConfig activityConfig) {
        activityConfig.setUpdateTime(new Date());
        activityConfig.setUpdateUser(JwtUtils.getUsername());
        activityConfigService.updateById(activityConfig);
        return Result.success();
    }
    @ApiOperation(value = "根据类型查询活动配置")
    @GetMapping(value = "/findActivityConfigByType")
    @ApiImplicitParam(name = "types", value = "活动类型：1签到，2借呗", required = true, paramType = "query", dataType = "int")
    public Result findActivityConfigByType(Integer types) {
        return Result.success(activityConfigService.findActivityConfigByType(types));
    }
}
