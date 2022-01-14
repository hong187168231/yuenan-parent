package com.indo.admin.modules.act.controller;


import com.indo.admin.modules.act.service.IActivityService;
import com.indo.admin.pojo.dto.ActivityDTO;
import com.indo.admin.pojo.dto.ActivityQueryDTO;
import com.indo.common.result.Result;
import com.indo.user.pojo.vo.act.ActivityVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 活动记录表 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Api(tags = "活动记录")
@RestController
@RequestMapping("/api/v1/act")
public class ActivityController {


    @Autowired
    private IActivityService iActivityService;

    @ApiOperation(value = "分页查询活动列表")
    @GetMapping(value = "/list")
    public Result<List<ActivityVo>> list(ActivityQueryDTO queryDTO) {
        return iActivityService.queryList(queryDTO);
    }

    @ApiOperation(value = "增加活动类型")
    @PostMapping(value = "/add")
    public Result add(ActivityDTO activityDTO) {
        return Result.judge(iActivityService.add(activityDTO));
    }


    @ApiOperation(value = "编辑活动类型")
    @PostMapping(value = "/edit")
    public Result edit(ActivityDTO activityDTO) {
        return Result.judge(iActivityService.edit(activityDTO));
    }


    @ApiOperation(value = "删除活动")
    @DeleteMapping(value = "/{actId}")
    @ApiImplicitParam(name = "actId", value = "活动id", required = true, paramType = "path", dataType = "long")
    public Result delete(@PathVariable Long actId) {
        return Result.judge(iActivityService.delAct(actId));
    }


    @ApiOperation(value = "活动上下架")
    @PutMapping(value = "/operateStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actId", value = "广告id", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "status", value = "状态 0 下架1 上架", required = true, paramType = "query", dataType = "int")
    })
    public Result operateStatus(@RequestParam("actId") Long actId, @RequestParam("status") Integer status) {
        return Result.judge(iActivityService.operateStatus(actId, status));
    }

}
