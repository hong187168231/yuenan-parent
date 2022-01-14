package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayTakeCashService;
import com.indo.admin.pojo.vo.pay.PayTakeCashApplyVO;
import com.indo.admin.pojo.vo.pay.PayTakeCashRecordVO;
import com.indo.common.result.Result;
import com.indo.pay.pojo.req.PayTakeCashReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
@Api(tags = "提现")
@RestController
@RequestMapping("/pay/takeCash")
public class PayTakeCashController {

    @Autowired
    private IPayTakeCashService iPayCashOrderService;


    @ApiOperation(value = "提现申请列表")
    @GetMapping(value = "/applyList")
    public Result<List<PayTakeCashApplyVO>> applyList(PayTakeCashReq cashOrderDTO) {
        Page<PayTakeCashApplyVO> result = iPayCashOrderService.cashApplyList(cashOrderDTO);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @ApiOperation(value = "提现记录")
    @GetMapping(value = "/recordList")
    public Result<List<PayTakeCashRecordVO>> cashList(PayTakeCashReq cashOrderDTO) {
        return iPayCashOrderService.cashRecordList(cashOrderDTO);
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
