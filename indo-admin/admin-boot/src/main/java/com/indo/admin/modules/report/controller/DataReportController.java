package com.indo.admin.modules.report.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.admin.modules.report.service.DataReportService;
import com.indo.admin.pojo.dto.AgentReportDTO;
import com.indo.admin.pojo.dto.MemReportDTO;
import com.indo.admin.pojo.vo.agent.AgentReportVo;
import com.indo.admin.pojo.vo.mem.MemReportVo;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 数据报表c
 */
@Api(tags = "数据报表接口")
@RestController
@RequestMapping("/api/v1/dataReport")
public class DataReportController {

    @Resource
    private DataReportService dataReportService;
    @Resource
    private IMemBaseinfoService iMemBaseInfoService;


    @ApiOperation(value = "查询会员报表")
    @GetMapping(value = "/memReportList")
    public Result<Page<MemReportVo>> memReportList(MemReportDTO memReportDTO) {
        return Result.success(dataReportService.findMemberReport(memReportDTO));
    }

    @ApiOperation(value = "查询代理报表")
    @GetMapping(value = "/agentReportPage")
    public Result<Page<AgentReportVo>> agentReportList(AgentReportDTO agentReportDTO) {
        return Result.success(dataReportService.findAgentReport(agentReportDTO));
    }

}
