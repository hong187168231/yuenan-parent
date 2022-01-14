package com.indo.admin.modules.agent.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.agent.service.IAgentCashApplyService;
import com.indo.admin.modules.agent.service.IAgentCashRecordService;
import com.indo.admin.pojo.req.agnet.AgentCashReq;
import com.indo.admin.pojo.vo.agent.AgentCashApplyVO;
import com.indo.admin.pojo.vo.agent.AgentCashRecordVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
@Api(tags = "代理提现接口")
@RestController
@RequestMapping("/agent/cash")
public class AgentTakeCashController {


    @Autowired
    private IAgentCashApplyService iAgentCashApplyService;

    @Autowired
    private IAgentCashRecordService iAgentCashRecordService;

    @ApiOperation(value = "代理提现申请")
    @PostMapping(value = "/applyList")
    public Result<List<AgentCashApplyVO>> applyList(AgentCashReq req) {
        Page<AgentCashApplyVO> result = iAgentCashApplyService.cashApplyList(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "代理提现记录")
    @PostMapping(value = "/recordList")
    public Result<List<AgentCashRecordVO>> recordList(AgentCashReq req) {
        Page<AgentCashRecordVO> result = iAgentCashRecordService.recordList(req);
        return Result.success(result.getRecords(), result.getTotal());
    }
}
