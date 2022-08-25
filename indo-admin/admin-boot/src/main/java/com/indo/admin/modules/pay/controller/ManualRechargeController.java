package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayManualRechargeService;
import com.indo.common.result.Result;
import com.indo.pay.pojo.vo.ManualRechargeMemVO;
import com.indo.pay.pojo.vo.ManualRechargeRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
@Api(tags = "人工充值")
@RestController
@RequestMapping("/pay/manual")
public class ManualRechargeController {

    @Autowired
    private IPayManualRechargeService iPayManualRechargeService;


    @ApiOperation(value = "可充值用户列表")
    @GetMapping(value = "/memList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "account", value = "用户账号", required = false, paramType = "query", dataType = "String"),
    })
    public Result<List<ManualRechargeMemVO>> memList(
            Long page,
            Long limit,
            String account) {
        Page<ManualRechargeMemVO> result = iPayManualRechargeService.memList(page, limit, account);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @ApiOperation(value = "操作充值")
    @PutMapping(value = "/operateRecharge")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operateType", value = "操作类型 1 加款 2 减款", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "memId", value = "会员id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "amount", value = "金额", required = true, paramType = "query", dataType = "float"),
            @ApiImplicitParam(name = "remarks", value = "备注", required = true, paramType = "query", dataType = "String"),
    })
    public Result operateRecharge(@Param("operateType") Integer operateType, @Param("memId") Long memId, @Param("amount") Float amount,@Param("remarks") String remarks) {
        boolean flag = iPayManualRechargeService.operateRecharge(operateType, memId, amount,remarks);
        return Result.judge(flag);
    }

    @ApiOperation(value = "人工充值记录")
    @GetMapping(value = "/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "account", value = "用户账号", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "operateType", value = "操作类型", required = false, paramType = "query", dataType = "int")
    })
    public Result<List<ManualRechargeRecordVO>> rechargeList(
            Integer page,
            Integer limit,
            String account,
            Integer operateType) {
        Page<ManualRechargeRecordVO> result = iPayManualRechargeService.queryList(page, limit, account, operateType);
        return Result.success(result.getRecords(), result.getTotal());
    }

}
