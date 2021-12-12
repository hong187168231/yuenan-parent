package com.indo.admin.modules.mem.controller;

import com.indo.admin.modules.mem.entity.MemAgentApply;
import com.indo.admin.modules.mem.req.MemAgentApplyPageReq;
import com.indo.admin.modules.mem.req.MemApplyAuditReq;
import com.indo.admin.modules.mem.service.IMemAgentApplyService;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员下级表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */

@Api(tags = "代理申请")
@RestController
@RequestMapping("/mem/agentApply")
public class MemAgentApplyController {

    @Autowired
    private IMemAgentApplyService memAgentApplyService;

    @ApiOperation(value = "申请列表", httpMethod = "POST")
    @PostMapping(value = "/page")
    @AllowAccess
    public Result<PageResult<MemAgentApply>> page(@RequestBody MemAgentApplyPageReq req) {
        memAgentApplyService.getPage(req);
        return Result.success();
    }

    @ApiOperation(value = "审核申请", httpMethod = "POST")
    @PostMapping(value = "/appltAudit")
    @AllowAccess
    public Result applyAudit(@RequestBody MemApplyAuditReq req) {
        memAgentApplyService.applyAudit(req);
        return Result.success();
    }
}
