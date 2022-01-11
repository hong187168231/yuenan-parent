package com.indo.admin.modules.mem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import com.indo.admin.modules.mem.service.IMemAgentService;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.AgentVo;
import com.indo.common.result.Result;
import com.indo.admin.modules.mem.req.SubordinateReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    public Result<List<AgentVo>> list(MemAgentPageReq req) {
        Page<AgentVo> result = memAgentService.getPage(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "下级列表", httpMethod = "GET")
    @GetMapping(value = "/subList")
    public Result<List<AgentSubVO>> subList(SubordinateReq req) {
        Page<AgentSubVO> result = memAgentService.subordinatePage(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "修改会员为代理")
    @PutMapping(value = "/upgradeAgent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "会员账号", paramType = "query", dataType = "string", required = true)
    })
    public Result upgradeAgent(@RequestParam("account") String account) {
        boolean flag = memAgentService.upgradeAgent(account);
        return Result.judge(flag);
    }


}
