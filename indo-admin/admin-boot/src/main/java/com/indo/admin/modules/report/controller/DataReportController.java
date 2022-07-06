package com.indo.admin.modules.report.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.admin.modules.report.service.DataReportService;
import com.indo.admin.pojo.dto.AgentReportDTO;
import com.indo.admin.pojo.dto.MemReportDTO;
import com.indo.admin.pojo.dto.PayRechargeReportDTO;
import com.indo.admin.pojo.dto.PlatformReportDTO;
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

/**
 * 数据报表c
 */
@Api(tags = "数据报表接口")
@RestController
@RequestMapping("/api/v1/dataReport")
public class DataReportController {

    @Resource
    private DataReportService dataReportService;


    @ApiOperation(value = "查询会员报表")
    @GetMapping(value = "/findMemReportPaghe")
    public Result<Page<MemReportVo>> findMemReportPaghe(MemReportDTO memReportDTO) {
        return Result.success(dataReportService.findMemberReportPage(memReportDTO));
    }

    @ApiOperation(value = "查询代理报表")
    @GetMapping(value = "/findAgentReportPage")
    public Result<Page<AgentReportVo>> findAgentReportPage(AgentReportDTO agentReportDTO) {
        return Result.success(dataReportService.findAgentReportPage(agentReportDTO));
    }

    @ApiOperation(value = "查询充值报表")
    @GetMapping(value = "/findPayRechargeReportPage")
    public Result<Page<PayRechargeReportVo>> findPayRechargeReportPage(PayRechargeReportDTO payRechargeReportDTO) {
        return Result.success(dataReportService.findPayRechargeReportPage(payRechargeReportDTO));
    }

    @ApiOperation(value = "查询平台报表")
    @GetMapping(value = "/findPlatformReportPage")
    public Result<Page<PlatformReportVo>> findPlatformReportPage(PlatformReportDTO platformReportDT) {
        return Result.success(dataReportService.findPlatformReportPage(platformReportDT));
    }
}
