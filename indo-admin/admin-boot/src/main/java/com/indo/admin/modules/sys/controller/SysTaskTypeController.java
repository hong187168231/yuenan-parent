package com.indo.admin.modules.sys.controller;


import com.indo.admin.pojo.dto.SysIpLimitDTO;
import com.indo.common.result.Result;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.dto.SysTaskTypeDTO;
import com.indo.core.pojo.entity.SysTaskType;
import com.indo.core.service.ISysTaskTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 任务类型表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
@Api(tags = "任务类型")
@RestController
@RequestMapping("/api/v1/sysTaskType")
public class SysTaskTypeController {
    @Resource
    private ISysTaskTypeService sysTaskTypeService;

    @ApiOperation(value = "查询任务类型分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页", name = "page",required = true,dataType = "int"),
            @ApiImplicitParam(value = "分页数", name = "limit",required = true,dataType = "int")
    })
    @GetMapping("/findSysTaskTypePage")
    public Result findSysTaskTypePage(SysTaskTypeDTO sysTaskTypeDT) {
        return Result.success(sysTaskTypeService.findSysTaskTypePage(sysTaskTypeDT));
    }
    @ApiOperation(value = "新增")
    @PostMapping("/insert")
    public Result insert(@RequestBody SysTaskType sysTaskType) {
        sysTaskType.setCreateTime(new Date());
        sysTaskType.setCreateUser(JwtUtils.getUsername());
        return Result.success(sysTaskTypeService.save(sysTaskType));
    }
    @ApiOperation(value = "修改")
    @PutMapping("/update")
    public Result update(@RequestBody SysTaskType sysTaskType) {
        sysTaskType.setUpdateTime(new Date());
        sysTaskType.setCreateUser(null);
        sysTaskTypeService.updateById(sysTaskType);
        return Result.success();
    }
    @ApiOperation(value = "删除")
    @ApiImplicitParam(value = "id", name = "iod",required = true,dataType = "int")
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        sysTaskTypeService.removeById(id);
        return Result.success();
    }
}
