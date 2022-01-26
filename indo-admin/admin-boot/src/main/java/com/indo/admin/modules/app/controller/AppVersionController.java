package com.indo.admin.modules.app.controller;


import com.indo.admin.modules.act.service.IActivityService;
import com.indo.admin.modules.app.service.IAppVersionService;
import com.indo.admin.pojo.dto.ActivityDTO;
import com.indo.admin.pojo.dto.ActivityQueryDTO;
import com.indo.admin.pojo.req.app.AppVersionReq;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.AppVersion;
import com.indo.user.pojo.vo.act.ActivityVo;
import com.indo.user.pojo.vo.app.AppVersionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
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
@Api(tags = "app版本管理")
@RestController
@RequestMapping("/api/v1/appVersion")
public class AppVersionController {


    @Autowired
    private IAppVersionService iAppVersionService;

    @ApiOperation(value = "appVersion列表")
    @GetMapping(value = "/list")
    public Result<List<AppVersionVo>> list() {
        return Result.success(iAppVersionService.queryList());
    }

    @ApiOperation(value = "增加appVersion")
    @PostMapping(value = "/add")
    public Result add(AppVersionReq versionReq) {
        return Result.judge(iAppVersionService.add(versionReq));
    }


    @ApiOperation(value = "编辑appVersion")
    @PostMapping(value = "/edit")
    public Result edit(AppVersionReq versionReq) {
        return Result.judge(iAppVersionService.edit(versionReq));
    }


    @ApiOperation(value = "删除appVersion")
    @DeleteMapping(value = "/{versionId}")
    @ApiImplicitParam(name = "versionId", value = "app版本id", required = true, paramType = "path", dataType = "int")
    public Result delete(@PathVariable Integer versionId) {
        return Result.judge(iAppVersionService.del(versionId));
    }


}
