package com.indo.pay.api.fallback;

import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.pay.api.WithdrawFeignClient;
import com.indo.pay.pojo.bo.PayTakeCashBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class WithdrawFeignFallback implements FallbackFactory<WithdrawFeignClient> {

    public static final String ERR_MSG = "代付feign接口暂时不可用: ";

    @Override
    public WithdrawFeignClient create(Throwable throwable) {
        throwable.printStackTrace();
        String msg = throwable == null ? "" : throwable.getMessage();
        if (!StringUtils.isEmpty(msg)) {
            log.error(msg);
        }
        return new WithdrawFeignClient() {
            @Override
            public Result<Boolean> withdrawRequest(PayTakeCashBO payTakeCash) {
                return null;
            }
        };
    }
}
