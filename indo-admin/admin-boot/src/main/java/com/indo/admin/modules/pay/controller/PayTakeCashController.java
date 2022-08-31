package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayTakeCashService;
import com.indo.admin.pojo.vo.pay.PayTakeCashApplyVO;
import com.indo.admin.pojo.vo.pay.PayTakeCashRecordVO;
import com.indo.common.enums.AudiTypeEnum;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.pay.pojo.req.PayTakeCashReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    public Result<List<PayTakeCash>> applyList(PayTakeCashReq cashOrderDTO) {
        Page<PayTakeCash> result = iPayCashOrderService.cashApplyList(cashOrderDTO);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @ApiOperation(value = "提现记录")
    @GetMapping(value = "/recordList")
    public Result<Page<PayTakeCash>> cashList(PayTakeCashReq cashOrderDTO) {
        return Result.success(iPayCashOrderService.cashRecordList(cashOrderDTO));
    }

    @ApiOperation(value = "提现状态操作")
    @PostMapping("/takeCashOpera")
    public Result takeCashOpera(
            @ApiParam("操作状态  agree=同意 reject=拒绝") @RequestParam(value = "audiType") AudiTypeEnum audiType,
            @ApiParam("提现id") @RequestParam(value = "takeCashId") Long takeCashId) {
        iPayCashOrderService.takeCashOpera(audiType, takeCashId);
        return Result.success();
    }


}
