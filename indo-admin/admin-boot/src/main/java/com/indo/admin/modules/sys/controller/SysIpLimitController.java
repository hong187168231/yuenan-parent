package com.indo.admin.modules.sys.controller;


import com.indo.admin.modules.sys.service.ISysIpLimitService;
import com.indo.admin.pojo.dto.SysIpLimitDTO;
import com.indo.admin.pojo.entity.SysIpLimit;
import com.indo.common.result.Result;
import com.indo.core.pojo.req.SysParameterReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 黑白名单IP限制表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-04-02
 */
@RestController
@RequestMapping("/api/v1/sysIpLimit")
public class SysIpLimitController {
    @Resource
    private ISysIpLimitService sysIpLimitService;

    @ApiOperation(value = "新增黑白名单")
    @PostMapping("/insertSysIpLimit")
    public Result insertSysIpLimit(@RequestBody SysIpLimit sysIpLimit) {
        sysIpLimitService.insertSysIpLimit(sysIpLimit);
        return Result.success();
    }
    @ApiOperation(value = "查询黑白名单列表分页及筛选")
    @PostMapping("/findSysIpLimitPage")
    public Result findSysIpLimitPage(@RequestBody SysIpLimitDTO sysIpLimit) {
        return Result.success(sysIpLimitService.findSysIpLimitPage(sysIpLimit));
    }
    @ApiOperation(value = "批量或单个删除黑白名单")
    @PostMapping("/deleteSysIpLimitByIdList")
    public Result deleteSysIpLimitByIdList(@RequestBody List<Integer> list) {
        sysIpLimitService.deleteSysIpLimitByIdList(list);
        return Result.success();
    }
}
