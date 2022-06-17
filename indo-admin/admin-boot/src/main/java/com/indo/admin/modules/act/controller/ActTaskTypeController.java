package com.indo.admin.modules.act.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.act.service.IActTaskTypeService;
import com.indo.admin.pojo.dto.ActTaskTypeDTO;
import com.indo.admin.pojo.dto.ActivityDTO;
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
 * 任务类型表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-06-16
 */
@Api(tags = "任务类型")
@RestController
@RequestMapping("/api/v1/taskType")
public class ActTaskTypeController {
    @Resource
    private IActTaskTypeService actTaskTypeService;

    @ApiOperation(value = "增加任务类型")
    @PostMapping(value = "/insert")
    public Result insert(@RequestBody ActTaskType actTaskType) {
        actTaskType.setCreateUser(JwtUtils.getUsername());
        actTaskType.setCreateTime(new Date());
        actTaskTypeService.save(actTaskType);
        return Result.success();
    }

    @ApiOperation(value = "删除任务类型")
    @GetMapping(value = "/delete")
    @ApiImplicitParam(name = "id", value = "任务类型ID", required = true,dataType = "long")
    public Result delete(Long id) {
        actTaskTypeService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "修改任务类型")
    @PutMapping(value = "/update")
    public Result update(@RequestBody ActTaskType actTaskType) {
        actTaskType.setUpdateUser(JwtUtils.getUsername());
        actTaskType.setUpdateTime(new Date());
        actTaskType.setCreateTime(null);
        actTaskType.setCreateUser(null);
        actTaskTypeService.updateById(actTaskType);
        return Result.success();
    }

    @GetMapping("/findPage")
    @ApiOperation("查询任务类型分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页", name = "page",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "分页数", name = "limit",required = true,paramType = "query",dataType = "int")
    })
    public Result<Page<ActTaskType>> findPage (ActTaskTypeDTO actTaskTypeDTO) {
        return Result.success(actTaskTypeService.findPage(actTaskTypeDTO));
    }
}
