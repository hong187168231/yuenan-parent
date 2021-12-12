package com.indo.user.controller;


import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemAgent;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;
import com.indo.user.pojo.req.mem.MemAgentStatReq;
import com.indo.user.pojo.req.mem.SubordinateReq;
import com.indo.user.pojo.vo.AgentStatVo;
import com.indo.user.pojo.vo.SubordinateVo;
import com.indo.user.service.IMemAgentService;
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
 * @since 2021-12-12
 */
@RestController
@RequestMapping("/mem/agent")
public class MemAgentController {

    @Autowired
    private IMemAgentService memAgentService;

    @ApiOperation(value = "代理统计", httpMethod = "POST")
    @PostMapping(value = "/agentStat")
    @AllowAccess
    public Result<AgentStatVo> agentStat(@RequestBody MemAgentStatReq req) {

        return Result.success(memAgentService.agentStat(req));
    }

    @ApiOperation(value = "下级列表", httpMethod = "POST")
    @PostMapping(value = "/subordinatePage")
    @AllowAccess
    public Result<PageResult<MemAgent>> subordinatePage(@RequestBody SubordinateReq req) {

        return Result.success(memAgentService.subordinatePage(req));
    }
}
