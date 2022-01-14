package com.indo.user.api.fallback;

import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.StringUtils;
import com.indo.user.api.MemBaseInfoFeignClient;
import com.indo.user.pojo.bo.MemTradingBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class MemBaseInfoFeignFallback implements FallbackFactory<MemBaseInfoFeignClient> {

    public static final String ERR_MSG = "Test接口暂时不可用: ";

    @Override
    public MemBaseInfoFeignClient create(Throwable throwable) {
        throwable.printStackTrace();
        String msg = throwable == null ? "" : throwable.getMessage();
        if (!StringUtils.isEmpty(msg)) {
            log.error(msg);
        }
        return new MemBaseInfoFeignClient() {

            @Override
            public Result<MemTradingBO> getMemTradingInfo(String account) {
                return Result.failed(ResultCode.DEGRADATION);
            }
        };
    }
}
