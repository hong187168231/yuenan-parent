package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayRechargeService;
import com.indo.admin.pojo.req.pay.PayRechargeReq;
import com.indo.admin.pojo.vo.pay.RechargeOrderVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api(tags = "充值")
@RestController
@RequestMapping("/pay/recharge")
public class PayRechargeController {

    @Autowired
    private IPayRechargeService rechargeOrderService;


    @ApiOperation(value = "充值记录")
    @GetMapping(value = "/list")
    public Result<List<RechargeOrderVO>> rechargeList(PayRechargeReq rechargeReq) {
        Page<RechargeOrderVO> result = rechargeOrderService.rechargeList(rechargeReq);
        return Result.success(result.getRecords(), result.getTotal());
    }

//
//    @ApiOperation(value = "提  现状态操作")
//    @PostMapping("/cashStatusOpera")
//
//    public Result cashStatusOpera(
//            @ApiParam("操作状态  1=确定 2=取消") @RequestParam(value = "operaStatus") Integer operaStatus,
//            @ApiParam("主键id") @RequestParam(value = "applyId") Long applyId) {
//        try {
//
//            cashBusiness.cashStatusOperation(loginUser, operaStatus, applyId);
//            return ResultInfo.ok();
//        }
//    }


}
