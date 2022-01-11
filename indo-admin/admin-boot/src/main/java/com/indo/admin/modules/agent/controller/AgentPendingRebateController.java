package com.indo.admin.modules.agent.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.agent.service.IAgentPendingRebateService;
import com.indo.admin.modules.mem.entity.MemPendingRebate;
import com.indo.admin.modules.mem.req.MemGrantRebateReq;
import com.indo.admin.modules.mem.req.MemPendingRebatePageReq;
import com.indo.admin.pojo.req.agnet.AgentPendingRebateReq;
import com.indo.admin.pojo.vo.agent.AgentPendingRebateVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
@Api(tags = "发放返点金额")
@RestController
@RequestMapping("/agent/pendingRebate")
public class AgentPendingRebateController {

    @Autowired
    private IAgentPendingRebateService iAgentPendingRebateService;

    @ApiOperation(value = "分页查询", httpMethod = "GET")
    @GetMapping(value = "/page")
    public Result<List<AgentPendingRebateVO>> getPage(AgentPendingRebateReq req) {
        Page<AgentPendingRebateVO> result = iAgentPendingRebateService.queryList(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "发放返点金额", httpMethod = "PUT")
    @ApiImplicitParam(name = "id", value = "待发放id", required = true, paramType = "query", dataType = "int")
    @PutMapping(value = "/grantRebate")
    public Result grantRebate(@Param("id") Long id) {
        boolean flag = iAgentPendingRebateService.grantRebate(id);
        return Result.judge(flag);
    }
}
