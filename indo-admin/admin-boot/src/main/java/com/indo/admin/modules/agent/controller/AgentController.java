package com.indo.admin.modules.agent.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.common.enums.AgentApplyEnum;
import com.indo.admin.modules.agent.service.IAgentApplyService;
import com.indo.admin.modules.agent.service.IMemAgentService;
import com.indo.admin.pojo.entity.AgentApply;
import com.indo.admin.pojo.entity.SysUser;
import com.indo.admin.pojo.vo.AgentApplyVO;
import com.indo.admin.pojo.vo.AgentDetailVO;
import com.indo.admin.pojo.vo.AgentVo;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayBankConfigDto;
import com.indo.pay.pojo.vo.PayBankConfigVO;
import com.indo.user.pojo.dto.AgentApplyDTO;
import com.indo.user.pojo.dto.AgentDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 代理管理 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-10-23
 */
@Api(tags = "代理管理")
@RestController
@RequestMapping("/api/v1/agent")
public class AgentController {


    @Autowired
    private IAgentApplyService iAgentApplyService;
    @Autowired
    private IMemAgentService iMemAgentService;

    @ApiOperation(value = "代理申请列表")
    @GetMapping(value = "/applyList")
    public Result<List<AgentApply>> getagentApplyList(AgentApplyDTO dto) {
        Page relust = iAgentApplyService.agentApplylist(dto);
        return Result.success(relust.getRecords(), relust.getTotal());
    }

    @ApiOperation(value = "代理申请操作")
    @GetMapping(value = "/applyOperate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agentApplyEnum", value = "操作类型枚举", paramType = "query", dataType = "String")
    })
    public Result applyOperate(AgentApplyEnum agentApplyEnum) {
        iAgentApplyService.applyOperate(agentApplyEnum);
        return Result.success();
    }


    @ApiOperation(value = "代理列表")
    @GetMapping(value = "/list")
    public Result<List<AgentVo>>  agentList(AgentDTO dto) {
        Page<AgentVo> page = new Page<>(dto.getPage(), dto.getLimit());
        List list =  iMemAgentService.agentlist(page,dto);
        return Result.success(list,page.getTotal());
    }


    @ApiOperation(value = "根据用户名查询代理")
    @GetMapping(value = "/queryAgentByUserName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "query", dataType = "String")
    })
    public Result queryAgentByNickName(String userName) {
        return Result.success(iMemAgentService.queryAgentByNickName(userName));
    }


    @ApiOperation(value = "添加代理")
    @GetMapping(value = "/addAgent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memId", value = "会员id", paramType = "query", dataType = "Long")
    })
    public Result addAgent(Long memId) {
        return Result.judge(iMemAgentService.addAgent(memId));
    }


    @ApiOperation(value = "修改代理")
    @GetMapping(value = "/updateAgent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agentId", value = "代理id", paramType = "query", dataType = "Long")
    })
    public Result updateAgent(Long memId) {
        return Result.judge(iMemAgentService.updateAgent(memId));
    }


    @ApiOperation(value = "查看代理")
    @GetMapping(value = "/agentDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agentId", value = "代理id", paramType = "query", dataType = "Long")
    })
    public Result<AgentDetailVO> agentDetail(Long memId) {
        return Result.success(iMemAgentService.agentDetail(memId));
    }
}
