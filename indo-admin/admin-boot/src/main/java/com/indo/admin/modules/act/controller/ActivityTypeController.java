package com.indo.admin.modules.act.controller;


import com.indo.admin.modules.act.service.IActivityTypeService;
import com.indo.admin.pojo.dto.ActivityTypeDTO;
import com.indo.admin.pojo.vo.ActivityTypeVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/act/activityType")
public class ActivityTypeController {


    @Autowired
    private IActivityTypeService iActivityTypeService;

    @ApiOperation(value = "分页查询活动类型")
    @GetMapping(value = "/list")
    public Result<List<ActivityTypeVO>> list(ActivityTypeDTO activityTypeDTO) {
        return iActivityTypeService.queryList(activityTypeDTO);
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

}
