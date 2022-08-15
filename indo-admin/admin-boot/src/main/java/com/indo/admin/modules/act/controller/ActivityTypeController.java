package com.indo.admin.modules.act.controller;


import com.indo.admin.modules.act.service.IActivityTypeService;
import com.indo.admin.pojo.dto.ActivityTypeDTO;
import com.indo.admin.pojo.vo.act.ActivityTypeVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 活动类型表 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Api(tags = "活动类型")
@RestController
@RequestMapping("/api/v1/actType")
public class ActivityTypeController {


    @Autowired
    private IActivityTypeService iActivityTypeService;

    @ApiOperation(value = "分页查询活动类型")
    @GetMapping(value = "/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, paramType = "query", dataType = "int")
    })
    public Result<List<ActivityTypeVO>> list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        return iActivityTypeService.queryList(page, limit);
    }

    @ApiOperation(value = "增加活动类型")
    @PostMapping(value = "/add")
    public Result add(ActivityTypeDTO activityTypeDTO) {
        return Result.judge(iActivityTypeService.add(activityTypeDTO));
    }


    @ApiOperation(value = "编辑活动类型")
    @PostMapping(value = "/edit")
    public Result edit(ActivityTypeDTO activityTypeDTO) {
        return Result.judge(iActivityTypeService.edit(activityTypeDTO));
    }

    @ApiOperation(value = "删除活动类型及旗下所有活动")
    @GetMapping(value = "/deleteActivityType")
    @ApiImplicitParam(name = "id", value = "活动类型ID", required = true,dataType = "int")
    public Result deleteActivityType(@RequestParam Integer id) {
        iActivityTypeService.deleteActivityType(id);
        return Result.success();
    }

}
