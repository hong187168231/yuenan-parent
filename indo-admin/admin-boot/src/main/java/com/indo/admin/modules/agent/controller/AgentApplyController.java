package com.indo.admin.modules.agent.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.agent.service.IAgentApplyService;
import com.indo.admin.pojo.req.agnet.MemAgentApplyReq;
import com.indo.admin.pojo.req.agnet.MemApplyAuditReq;
import com.indo.admin.pojo.vo.agent.AgentApplyVO;
import com.indo.common.result.Result;
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
 * @since 2021-11-19
 */

@Api(tags = "代理申请")
@RestController
@RequestMapping("/mem/agentApply")
public class AgentApplyController {

    @Autowired
    private IAgentApplyService agentApplyService;

    @ApiOperation(value = "申请列表", httpMethod = "GET")
    @GetMapping(value = "/page")
    public Result<List<AgentApplyVO>> page(MemAgentApplyReq req) {
        Page<AgentApplyVO> result = agentApplyService.getPage(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "审核申请", httpMethod = "PUT")
    @PutMapping(value = "/appltAudit")
    public Result applyAudit(@RequestBody MemApplyAuditReq req) {
        boolean flag = agentApplyService.applyAudit(req);
        return Result.judge(flag);
    }
}
