package com.indo.admin.modules.stat.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.req.MemBaseInfoPageReq;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.admin.modules.stat.entity.StatDailyBase;
import com.indo.admin.modules.stat.req.TotalStatReq;
import com.indo.admin.modules.stat.req.UserReportReq;
import com.indo.admin.modules.stat.service.IStatDailyBaseService;
import com.indo.admin.modules.stat.vo.UserReportVo;
import com.indo.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-01-11
 */
@RestController
@RequestMapping("/stat/daily-base")
public class StatDailyBaseController {

    @Autowired
    private IStatDailyBaseService statDailyBaseService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/listByPage")
    public Result<Page<UserReportVo>> getMemBaseInfo(@RequestBody UserReportReq req) {
        Page<UserReportVo> result = statDailyBaseService.queryList(req);
        return Result.success(result);
    }

    @ApiOperation(value = "首页统计总报表")
    @PostMapping(value = "/listByPage")
    public Result<StatDailyBase> totalStat(@RequestBody TotalStatReq req) {
        StatDailyBase result = statDailyBaseService.totalStat(req);
        return Result.success(result);
    }
}