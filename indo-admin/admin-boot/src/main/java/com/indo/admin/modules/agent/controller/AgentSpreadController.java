package com.indo.admin.modules.agent.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.agent.service.IAgentSpreadService;
import com.indo.admin.pojo.req.agnet.AgentSpreadReq;
import com.indo.admin.pojo.req.agnet.MemAgentReq;
import com.indo.admin.pojo.vo.agent.AgentVo;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 代理推广表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-03-11
 */
@Api(tags = "代理推广")
@RestController
@RequestMapping("/agent/spread")
public class AgentSpreadController {
  @Resource
  private IAgentSpreadService agentSpreadService;

    @ApiOperation(value = "代理推广列表", httpMethod = "GET")
    @GetMapping(value = "/list")
    public Result list(AgentSpreadReq req) {
        return Result.success(agentSpreadService.findAgentSpreadPage(req));
    }

    @ApiOperation(value = "新增代理推广信息", httpMethod = "POST")
    @PostMapping(value = "/insert")
    public Result insert(@RequestBody AgentSpreadReq req) {
        agentSpreadService.insertAgentSpread(req);
        return Result.success();
    }

    @ApiOperation(value = "修改代理推广信息", httpMethod = "POST")
    @PostMapping(value = "/update")
    public Result update(@RequestBody AgentSpreadReq req) {
        agentSpreadService.updateAgentSpread(req);
        return Result.success();
    }
}
