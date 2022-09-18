package com.indo.admin.modules.agent.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.agent.service.IAgentRelationService;
import com.indo.admin.pojo.req.agnet.MemAgentReq;
import com.indo.admin.pojo.req.agnet.SubordinateReq;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.AgentVo;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
public class AgentRelationController {

    @Autowired
    private IAgentRelationService agentRelationService  ;

    @ApiOperation(value = "代理列表", httpMethod = "GET")
    @GetMapping(value = "/list")
    public Result<List<AgentVo>> list(MemAgentReq req) {
        Page<AgentVo> result = agentRelationService.getPage(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "下级列表", httpMethod = "GET")
    @GetMapping(value = "/subList")
    public Result<List<AgentSubVO>> subList(SubordinateReq req) {
        Page<AgentSubVO> result = agentRelationService.subordinatePage(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "修改会员为代理")
    @PutMapping(value = "/upgradeAgent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "会员账号", paramType = "query", dataType = "string", required = true)
    })
    public Result upgradeAgent(@RequestParam("account") String account, HttpServletRequest request) {
        boolean flag = agentRelationService.upgradeAgent(account,request);
        return Result.judge(flag);
    }


}
