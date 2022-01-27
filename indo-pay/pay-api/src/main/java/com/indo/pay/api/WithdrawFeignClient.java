package com.indo.pay.api;


import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import com.indo.common.web.exception.KeepErrMsgConfiguration;
import com.indo.pay.api.fallback.WithdrawFeignFallback;
import com.indo.pay.pojo.bo.PayTakeCashBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ServiceIdConstant.PAY_SERVICE_ID,
        fallbackFactory = WithdrawFeignFallback.class, configuration = {KeepErrMsgConfiguration.class})
public interface WithdrawFeignClient {


    @GetMapping("/rpc/withdraw/withdrawRequest")
    Result<Boolean> withdrawRequest(@RequestBody PayTakeCashBO payTakeCash);

}
