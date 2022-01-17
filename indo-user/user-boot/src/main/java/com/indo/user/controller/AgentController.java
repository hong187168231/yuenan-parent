package com.indo.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.agnet.AgentRebateRecordReq;
import com.indo.admin.pojo.vo.mem.MemBetVo;
import com.indo.admin.pojo.vo.agent.AgentRebateInfoVO;
import com.indo.admin.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.constant.RedisKeys;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.common.web.util.DozerUtil;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;
import com.indo.user.pojo.req.mem.SubordinateAppReq;
import com.indo.user.service.IMemAgentService;
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

@Api(tags = "代理接口")
@RestController
@RequestMapping("/mem/agent")
public class AgentController {

    @Autowired
    private IMemAgentService iMemAgentService;

    @ApiOperation(value = "申请", httpMethod = "POST")
    @PostMapping(value = "/apply")
    public Result add(@RequestBody MemAgentApplyReq req, @LoginUser LoginInfo loginInfo) {
        iMemAgentService.apply(req, loginInfo);
        return Result.success();
    }

    @ApiOperation(value = "我的佣金", httpMethod = "GET")
    @GetMapping(value = "/rebateInfo")
    public Result<AgentRebateInfoVO> rebateInfo(@LoginUser LoginInfo loginInfo) {
        AgentRebateInfoVO infoVO = iMemAgentService.rebateInfo(loginInfo);
        return Result.success(infoVO);
    }


    @ApiOperation(value = "获取返佣配置", response = MemBetVo.class, httpMethod = "GET")
    @GetMapping(value = "/rebateConfig")
    @AllowAccess
    public Result<List<MemBetVo>> rebateConfig() {
        List<MemBetVo> list = RedisUtils.get(RedisKeys.SYS_REBATE_KEY);
        return Result.success(list);
    }

    @ApiOperation(value = "佣金列表", httpMethod = "GET")
    @GetMapping(value = "/rebateList")
    public Result rebateList(@RequestBody AgentRebateRecordReq req, @LoginUser LoginInfo loginInfo) {
        Page<AgentRebateRecordVO> result = iMemAgentService.queryList(req, loginInfo);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @ApiOperation(value = "下级佣金明细", httpMethod = "GET")
    @GetMapping(value = "/subRebateList")
    public Result subRebateList(@RequestBody AgentRebateRecordReq req, @LoginUser LoginInfo loginInfo) {
        Page<AgentRebateRecordVO> result = iMemAgentService.subRebateList(req, loginInfo);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @ApiOperation(value = "下级列表", httpMethod = "GET")
    @GetMapping(value = "/subList")
    public Result<List<AgentSubVO>> subList(@RequestBody SubordinateAppReq req, @LoginUser LoginInfo loginInfo) {
        Page<AgentSubVO> result = iMemAgentService.subordinatePage(req, loginInfo);
        return Result.success(result.getRecords(), result.getTotal());
    }


}
