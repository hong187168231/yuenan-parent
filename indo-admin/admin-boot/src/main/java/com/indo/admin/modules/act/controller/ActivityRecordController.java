package com.indo.admin.modules.act.controller;


import com.indo.admin.modules.act.service.IActivityRecordService;
import com.indo.admin.modules.act.service.IActivityTypeService;
import com.indo.admin.pojo.dto.ActivityRecordDTO;
import com.indo.admin.pojo.dto.ActivityTypeDTO;
import com.indo.admin.pojo.vo.ActivityRecordVO;
import com.indo.admin.pojo.vo.ActivityTypeVO;
import com.indo.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 活动记录表 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@RestController
@RequestMapping("/act/activityRecord")
public class ActivityRecordController {


    @Autowired
    private IActivityRecordService iActivityRecordService;

    @ApiOperation(value = "分页查询活动类型")
    @GetMapping(value = "/list")
    public Result<List<ActivityRecordVO>> list(ActivityRecordDTO activityRecordDTO) {
        return iActivityRecordService.queryList(activityRecordDTO);
    }

    @ApiOperation(value = "增加活动类型")
    @PostMapping(value = "/add")
    public Result add(ActivityRecordDTO activityRecordDTO) {
        return Result.judge(iActivityRecordService.add(activityRecordDTO));
    }


    @ApiOperation(value = "编辑活动类型")
    @PostMapping(value = "/edit")
    public Result edit(ActivityRecordDTO activityRecordDTO) {
        return Result.judge(iActivityRecordService.edit(activityRecordDTO));
    }

}
