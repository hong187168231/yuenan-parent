package com.indo.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.pojo.req.agent.AgentRebateRecordReq;
import com.indo.admin.pojo.vo.agent.*;
import com.indo.core.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.core.pojo.vo.mem.MemBetVo;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.constant.RedisKeys;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;
import com.indo.user.pojo.req.mem.SubordinateAppReq;
import com.indo.user.service.IMemAgentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 会员下级表 前端控制器
 * </p>
 *
 * @author puff
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
    public Result add(@RequestBody MemAgentApplyReq req, @LoginUser LoginInfo loginInfo, HttpServletRequest request) {
        iMemAgentService.apply(req, loginInfo, request);
        return Result.success();
    }

    @ApiOperation(value = "代理申请状态", httpMethod = "GET")
    @GetMapping(value = "/applyStatus")
    public Result<ApplyStatusVO> applyStatus(@LoginUser LoginInfo loginInfo) {
        Integer status = iMemAgentService.applyStatus(loginInfo);
        return Result.success(new ApplyStatusVO(status));
    }

    @ApiOperation(value = "我的佣金", httpMethod = "GET")
    @GetMapping(value = "/rebateInfo")
    public Result<AgentRebateInfoVO> rebateInfo(@LoginUser LoginInfo loginInfo) {
        AgentRebateInfoVO infoVO = iMemAgentService.rebateInfo(loginInfo);
        return Result.success(infoVO);
    }


    @ApiOperation(value = "佣金提现", httpMethod = "POST")
    @PostMapping(value = "/takeRebate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rebateAmount", value = "佣金", defaultValue = "0.00", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "memBankId", value = "会员银行卡id", paramType = "query", dataType = "long")
    })
    public Result takeRebate(BigDecimal rebateAmount, Long memBankId, @LoginUser LoginInfo loginInfo,
                             HttpServletRequest request) {
        boolean flag = iMemAgentService.takeRebate(rebateAmount, memBankId, loginInfo, request);
        return Result.judge(flag);
    }


    @ApiOperation(value = "获取返佣配置", response = MemBetVo.class, httpMethod = "GET")
    @GetMapping(value = "/rebateConfig")
    @AllowAccess
    public Result<List<MemBetVo>> rebateConfig() {
        List<MemBetVo> list = RedisUtils.get(RedisKeys.SYS_REBATE_KEY);
        return Result.success(list);
    }

    @ApiOperation(value = "佣金明细", httpMethod = "GET")
    @GetMapping(value = "/rebateList")
    public Result<List<AgentRebateRecordVO>> rebateList(AgentRebateRecordReq req, @LoginUser LoginInfo loginInfo) {
        Page<AgentRebateRecordVO> result = iMemAgentService.queryList(req, loginInfo);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @ApiOperation(value = "下级列表", httpMethod = "GET")
    @GetMapping(value = "/subList")
    public Result<List<AgentSubVO>> subList(SubordinateAppReq req, @LoginUser LoginInfo loginInfo) {
        Page<AgentSubVO> result = iMemAgentService.subordinatePage(req, loginInfo);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "佣金报表统计", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginTime", value = "开始时间", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, paramType = "query", dataType = "string")
    })
    @GetMapping(value = "/rebateStat")
    public Result<RebateStatVO> rebateStat(String beginTime, String endTime, @LoginUser LoginInfo loginInfo) {
        RebateStatVO result = iMemAgentService.rebateStat(beginTime, endTime, loginInfo);
        return Result.success(result);
    }


}
