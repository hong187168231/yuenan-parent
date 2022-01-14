package com.indo.admin.modules.stat.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.entity.StatPlatReport;
import com.indo.admin.modules.stat.req.PlatReportReq;
import com.indo.admin.modules.stat.service.IStatPlatReportService;
import com.indo.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-01-11
 */
@RestController
@RequestMapping("/stat/plat-report")
public class StatPlatReportController {

    @Autowired
    private IStatPlatReportService statPlatReportService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/listByPage")
    public Result<Page<StatPlatReport>> getMemBaseInfo(@RequestBody PlatReportReq req) {
        Page<StatPlatReport> result = statPlatReportService.queryList(req);
        return Result.success(result);
    }
}
