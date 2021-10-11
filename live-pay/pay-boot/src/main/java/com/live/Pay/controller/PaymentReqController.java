package com.live.Pay.controller;

import com.live.Pay.service.IPaymentReqService;
import com.live.common.annotation.LoginUser;
import com.live.common.pojo.bo.LoginInfo;
import com.live.common.result.Result;
import com.live.pay.pojo.req.PayOnlineReq;
import com.live.pay.pojo.req.RechargeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @创建人 puff
 * @创建时间 2021/4/5 15:57
 * @描述 充值controller
 */
@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentReqController {

    @Autowired
    private IPaymentReqService paymentReqService;


    @PostMapping("/v1/onlineRecharge")
    public Result onlineRecharge(@RequestBody RechargeReq rechargeReq) {
        return Result.success();
    }


    /**
     * 扫码支付
     * @param payOnlineReq
     * @return
     */
    @GetMapping("/v1/payway")
    public Result getPayWayList(@LoginUser LoginInfo loginInfo, @RequestBody PayOnlineReq payOnlineReq){
        payOnlineReq.setMemId(loginInfo.getId());

        return Result.success(paymentReqService.getPayWayList(payOnlineReq));
    }

    /**
     * 银行卡支付
     * @param payOnlineReq
     * @return
     */
    @GetMapping("/v1/paybank")
    public Result getPayBankList(@LoginUser LoginInfo loginInfo, @RequestBody PayOnlineReq payOnlineReq){
        payOnlineReq.setMemId(loginInfo.getId());
        return Result.success(paymentReqService.getPayBankList(payOnlineReq));
    }

}
