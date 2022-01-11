package com.indo.admin.modules.stat.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.stat.entity.StatAgentReport;
import com.indo.admin.modules.stat.req.AgentReportReq;
import com.indo.admin.modules.stat.req.UserReportReq;
import com.indo.admin.modules.stat.service.IStatAgentReportService;
import com.indo.admin.modules.stat.vo.UserReportVo;
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
@RequestMapping("/stat/agent-report")
public class StatAgentReportController {

    @Autowired
    private IStatAgentReportService statAgentReportService;


    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/listByPage")
    public Result<Page<StatAgentReport>> getMemBaseInfo(@RequestBody AgentReportReq req) {
        Page<StatAgentReport> result = statAgentReportService.queryList(req);
        return Result.success(result);
    }
}
