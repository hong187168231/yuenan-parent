package com.indo.admin.modules.mem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.entity.MemAgent;
import com.indo.admin.modules.mem.entity.MemAgentApply;
import com.indo.admin.modules.mem.req.MemAgentApplyPageReq;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import com.indo.admin.modules.mem.service.IMemAgentService;
import com.indo.admin.modules.mem.vo.AgentVo;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.admin.modules.mem.req.SubordinateReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 会员下级表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
@Api(tags = "代理接口")
@RestController
@RequestMapping("/mem/agent")
public class MemAgentController {

    @Autowired
    private IMemAgentService memAgentService;

    @ApiOperation(value = "代理列表", httpMethod = "GET")
    @GetMapping(value = "/list")
    public Result<List<AgentVo>> list(@RequestBody MemAgentPageReq req) {
        Page<AgentVo> result = memAgentService.getPage(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "下级列表", httpMethod = "GET")
    @GetMapping(value = "/subList")
    public Result<List<MemBaseInfoVo>> subList(@RequestBody SubordinateReq req) {
        Page<MemBaseInfoVo> result = memAgentService.subordinatePage(req);
        return Result.success(result.getRecords(), result.getTotal());
    }
}
