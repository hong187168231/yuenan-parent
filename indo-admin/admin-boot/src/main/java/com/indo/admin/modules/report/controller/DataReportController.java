package com.indo.admin.modules.report.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.AgentReportDto;
import com.indo.user.pojo.dto.MemReportDto;
import com.indo.user.pojo.vo.AgentReportVo;
import com.indo.user.pojo.vo.MemReportVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 数据报表c
 */
@Api(tags = "数据报表接口")
@RestController
@RequestMapping("/api/v1/dataReport")
public class DataReportController {


    @Resource
    private IMemBaseinfoService iMemBaseInfoService;


    @ApiOperation(value = "查询会员报表")
    @GetMapping(value = "/memReportList")
    public Result<PageResult<MemReportVo>> memReportList(MemReportDto dto) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != dto.getPage() && null != dto.getLimit()) {
            pageNum = dto.getPage();
            pageSize = dto.getLimit();
        }
        Page<MemReportVo> page = new Page<>(pageNum, pageSize);
//        List<MemReportVo> list = iMemBaseInfoService.memReportList(page, dto);
//        page.setRecords(list);
        return Result.success(PageResult.getPageResult(page));
    }

    @ApiOperation(value = "导出会员报表")
    @ApiImplicitParam(name = "ids", value = "用户ID，逗号拼接(1,2)")
    @GetMapping(value = "/memReportExport")
    public void memReportExport(HttpServletResponse response, @RequestParam(required = false) List<Long> ids) throws IOException {
//        iMemBaseInfoService.memReportExport(response, ids);
    }

    @ApiOperation(value = "查询代理报表")
    @GetMapping(value = "/agentReportList")
    public Result<PageResult<AgentReportVo>> agentReportList(AgentReportDto dto) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != dto.getPage() && null != dto.getLimit()) {
            pageNum = dto.getPage();
            pageSize = dto.getLimit();
        }
        Page<AgentReportVo> page = new Page<>(pageNum, pageSize);
//        List<AgentReportVo> list = iMemBaseInfoService.agentReportList(page, dto);
//        page.setRecords(list);
        return Result.success(PageResult.getPageResult(page));
    }

    @ApiOperation(value = "导出代理报表")
    @ApiImplicitParam(name = "ids", value = "用户ID，逗号拼接(1,2)")
    @GetMapping(value = "/agentReportExport")
    public void agentReportExport(HttpServletResponse response, @RequestParam(required = false) List<Long> ids) throws IOException {
//        iMemBaseInfoService.agentReportExport(response, ids);
    }
}
