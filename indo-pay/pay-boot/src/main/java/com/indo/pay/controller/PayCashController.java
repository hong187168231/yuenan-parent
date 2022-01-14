package com.indo.pay.controller;


import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayCashOrderDTO;
import com.indo.pay.pojo.req.CashApplyReq;
import com.indo.pay.pojo.vo.PayCashOrderVO;
import com.indo.pay.pojo.vo.PayChannelVO;
import com.indo.pay.service.IPayCashOrderService;
import com.indo.pay.service.IPayChannelConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 支付渠道配置 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Api(tags = "提现")
@RestController
@RequestMapping("/pay/cash")
public class PayCashController {

    @Resource
    private IPayCashOrderService iPayCashOrderService;


    @ApiOperation(value = "提现申请")
    @PostMapping(value = "/apply")
    public Result cashApply(CashApplyReq cashApplyReq, @LoginUser LoginInfo loginInfo) {
        boolean flag = iPayCashOrderService.cashApply(cashApplyReq, loginInfo);
        return Result.judge(flag);
    }


    @ApiOperation(value = "提现记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "Long")
    })
    @GetMapping(value = "/list")
    public Result<List<PayCashOrderVO>> cashList(Integer page,
                                                 Integer limit, @LoginUser LoginInfo loginInfo) {
        return iPayCashOrderService.cashRecordList(page, limit, loginInfo);
    }


}
