package com.indo.admin.modules.report.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.admin.modules.report.service.DataReportService;
import com.indo.admin.pojo.dto.*;
import com.indo.admin.pojo.vo.TotalReportVo;
import com.indo.admin.pojo.vo.agent.AgentReportVo;
import com.indo.admin.pojo.vo.game.PlatformReportVo;
import com.indo.admin.pojo.vo.mem.MemReportVo;
import com.indo.admin.pojo.vo.pay.PayRechargeReportVo;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 数据报表
 */
@Api(tags = "数据报表接口")
@RestController
@RequestMapping("/api/v1/dataReport")
public class DataReportController {

    @Resource
    private DataReportService dataReportService;


    @ApiOperation(value = "查询会员报表")
    @GetMapping(value = "/findMemReportPaghe")
    public Result<Page<MemReportVo>> findMemReportPaghe(MemReportDTO memReportDTO, HttpServletRequest request) {
        return Result.success(dataReportService.findMemberReportPage(memReportDTO,request));
    }

    @ApiOperation(value = "查询代理报表")
    @GetMapping(value = "/findAgentReportPage")
    public Result<Page<AgentReportVo>> findAgentReportPage(AgentReportDTO agentReportDTO, HttpServletRequest request) {
        return Result.success(dataReportService.findAgentReportPage(agentReportDTO,request));
    }

    @ApiOperation(value = "查询充值报表")
    @GetMapping(value = "/findPayRechargeReportPage")
    public Result<Page<PayRechargeReportVo>> findPayRechargeReportPage(PayRechargeReportDTO payRechargeReportDTO, HttpServletRequest request) {
        return Result.success(dataReportService.findPayRechargeReportPage(payRechargeReportDTO,request));
    }

    @ApiOperation(value = "查询平台报表")
    @GetMapping(value = "/findPlatformReportPage")
    public Result<Page<PlatformReportVo>> findPlatformReportPage(PlatformReportDTO platformReportDT, HttpServletRequest request) {
        return Result.success(dataReportService.findPlatformReportPage(platformReportDT,request));
    }

    @ApiOperation(value = "查询总表")
    @GetMapping(value = "/findTotalReport")
    public Result<TotalReportVo> findTotalReport(TotalReportDTO totalReportDTO, HttpServletRequest request) {
        return Result.success(dataReportService.findTotalReport(totalReportDTO,request));
    }
}
