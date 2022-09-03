package com.indo.admin.modules.agent.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.agent.service.IAgentRebateRecordService;
import com.indo.core.pojo.req.agent.AgentRebateRecordReq;
import com.indo.core.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api(tags = "用户返点记录")
@RestController
@RequestMapping("/agent/rebateRecord")
public class AgentRebateRecordController {


    @Autowired
    private IAgentRebateRecordService iAgentRebateRecordService;

    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/page")
    public Result<List<AgentRebateRecordVO>> getPage(AgentRebateRecordReq req) {
        Page<AgentRebateRecordVO> result = iAgentRebateRecordService.queryList(req);
        return Result.success(result.getRecords(), result.getTotal());
    }
}
