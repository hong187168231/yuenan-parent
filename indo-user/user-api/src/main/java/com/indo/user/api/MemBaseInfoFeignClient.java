package com.indo.user.api;


import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.common.web.exception.KeepErrMsgConfiguration;
import com.indo.user.api.fallback.MemBaseInfoFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(value = ServiceIdConstant.USER_SERVICE_ID, contextId = "oauth-client",
        fallbackFactory = MemBaseInfoFeignFallback.class, configuration = {KeepErrMsgConfiguration.class})
public interface MemBaseInfoFeignClient {


    @GetMapping("/rpc/memBaseInfo/getMemTradingInfo/{account}")
    Result<MemTradingBO> getMemTradingInfo(@PathVariable String account);


    @PostMapping("/rpc/memBaseInfo/takeCash/apply")
    Result<Boolean> takeCashApply(@RequestParam("account")String account, @RequestParam("amount")BigDecimal amount);

}
