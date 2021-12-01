package com.indo.admin.modules.pay.controller;


import com.indo.admin.modules.pay.service.IPayBankService;
import com.indo.admin.modules.pay.service.IPayCashOrderService;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayBankDTO;
import com.indo.pay.pojo.dto.PayCashOrderDTO;
import com.indo.pay.pojo.vo.PayBankVO;
import com.indo.pay.pojo.vo.PayCashOrderApplyVO;
import com.indo.pay.pojo.vo.PayCashOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
@Api(tags = "提现")
@RestController
@RequestMapping("/pay/takeCash")
public class PayCashOrderController {

    @Autowired
    private IPayCashOrderService iPayCashOrderService;




    @ApiOperation(value = "提现申请列表")
    @GetMapping(value = "/applyList")
    public Result<List<PayCashOrderApplyVO>> applyList(PayCashOrderDTO cashOrderDTO) {
        return iPayCashOrderService.cashApplyList(cashOrderDTO);
    }


    @ApiOperation(value = "提现记录")
    @GetMapping(value = "/cashList")
    public Result<List<PayCashOrderVO>> cashList(PayCashOrderDTO cashOrderDTO) {
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
