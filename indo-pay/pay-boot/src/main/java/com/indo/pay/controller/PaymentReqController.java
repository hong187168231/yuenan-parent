package com.indo.pay.controller;

import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.pay.pojo.req.PayOnlineReq;
import com.indo.pay.pojo.req.RechargeReq;
import com.indo.pay.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @创建人 puff
 * @创建时间 2021/4/5 15:57
 * @描述 充值controller
 */
@Api(tags = "app支付支付")
@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentReqController {

    @Autowired
    private PaymentService paymentService;

    @ApiOperation(value = "发起支付")
    @PostMapping("/v1/onlineRecharge")
    public Result onlineRecharge(@RequestBody RechargeReq rechargeReq, @LoginUser LoginInfo loginInfo) {
        return paymentService.paymentRequestByUser(loginInfo, rechargeReq);
    }


//    /**
//     * 扫码支付
//     * @param payOnlineReq
//     * @return
//     */
//    @GetMapping("/v1/payway")
//    public Result getPayWayList(@LoginUser LoginInfo loginInfo, @RequestBody PayOnlineReq payOnlineReq){
//        payOnlineReq.setMemId(loginInfo.getId());
//
//        return Result.success(paymentReqService.getPayWayList(payOnlineReq));
//    }
//
//    /**
//     * 银行卡支付
//     * @param payOnlineReq
//     * @return
//     */
//    @GetMapping("/v1/paybank")
//    public Result getPayBankList(@LoginUser LoginInfo loginInfo, @RequestBody PayOnlineReq payOnlineReq){
//        payOnlineReq.setMemId(loginInfo.getId());
//        return Result.success(paymentReqService.getPayBankList(payOnlineReq));
//    }

}
