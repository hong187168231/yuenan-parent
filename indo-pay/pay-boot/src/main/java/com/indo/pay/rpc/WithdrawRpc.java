package com.indo.pay.rpc;

import com.indo.common.result.Result;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.pay.pojo.bo.PayTakeCashBO;
import com.indo.pay.service.proxyWithdraw.ProxyWithdrawService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * <p>
 * 用户信息 rpc控制器
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@RestController
@RequestMapping("/rpc/withdraw")
public class WithdrawRpc {

    @Resource
    private ProxyWithdrawService proxyWithdrawService;

    @GetMapping("/withdrawRequest")
    public Result<Boolean> getMemTradingInfo(@RequestBody PayTakeCashBO payTakeCashBO) {
        return Result.success(proxyWithdrawService.withdrawRequest(payTakeCashBO));
    }


}
    