package com.indo.admin.modules.sys.controller;


import com.indo.common.result.Result;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.dto.SysTaskDTO;
import com.indo.core.pojo.dto.SysTaskTypeDTO;
import com.indo.core.pojo.entity.SysTask;
import com.indo.core.pojo.entity.SysTaskType;
import com.indo.core.service.ISysTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 任务表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
@Api(tags = "任务")
@RestController
@RequestMapping("/api/v1/sysTask")
public class SysTaskController {
    @Resource
    private ISysTaskService sysTaskService;

    @ApiOperation(value = "查询任务分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页", name = "page",required = true,dataType = "int"),
            @ApiImplicitParam(value = "分页数", name = "limit",required = true,dataType = "int")
    })
    @GetMapping("/findSysTaskPage")
    public Result findSysTaskPage(SysTaskDTO sysTaskDTO) {
        return Result.success(sysTaskService.findSysTaskPage(sysTaskDTO));
    }
    @ApiOperation(value = "新增")
    @PostMapping("/insert")
    public Result insert(@RequestBody SysTask sysTask) {
        sysTask.setCreateTime(new Date());
        sysTask.setCreateUser(JwtUtils.getUsername());
        return Result.success(sysTaskService.save(sysTask));
    }
    @ApiOperation(value = "修改")
    @PutMapping("/update")
    public Result update(@RequestBody SysTask sysTask) {
        sysTask.setUpdateTime(new Date());
        sysTask.setCreateUser(null);
        sysTaskService.updateById(sysTask);
        return Result.success();
    }
    @ApiOperation(value = "删除")
    @ApiImplicitParam(value = "id", name = "iod",required = true,dataType = "int")
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        sysTaskService.removeById(id);
        return Result.success();
    }
}
