package com.indo.admin.modules.act.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.act.service.IActTaskService;
import com.indo.admin.pojo.dto.ActTaskDTO;
import com.indo.admin.pojo.dto.ActTaskTypeDTO;
import com.indo.admin.pojo.entity.ActTask;
import com.indo.admin.pojo.entity.ActTaskType;
import com.indo.common.result.Result;
import com.indo.common.web.util.JwtUtils;
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
 * @since 2022-06-16
 */
@Api(tags = "任务信息")
@RestController
@RequestMapping("/api/v1/task")
public class ActTaskController {
    @Resource
    private IActTaskService actTaskService;

    @ApiOperation(value = "增加任务")
    @PostMapping(value = "/insert")
    public Result insert(@RequestBody ActTask actTask) {
        actTask.setCreateUser(JwtUtils.getUsername());
        actTask.setCreateTime(new Date());
        actTaskService.save(actTask);
        return Result.success();
    }

    @ApiOperation(value = "删除任务")
    @GetMapping(value = "/delete")
    @ApiImplicitParam(name = "id", value = "任务ID", required = true,dataType = "long")
    public Result delete(Long id) {
        actTaskService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "修改任务类型")
    @PutMapping(value = "/update")
    public Result update(@RequestBody ActTask actTask) {
        actTask.setUpdateUser(JwtUtils.getUsername());
        actTask.setUpdateTime(new Date());
        actTask.setCreateTime(null);
        actTask.setCreateUser(null);
        actTaskService.updateById(actTask);
        return Result.success();
    }

    @GetMapping("/findPage")
    @ApiOperation("查询任务分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页", name = "page",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "分页数", name = "limit",required = true,paramType = "query",dataType = "int")
    })
    public Result<Page<ActTask>> findPage (ActTaskDTO actTaskDTO) {
        return Result.success(actTaskService.findPage(actTaskDTO));
    }
}
