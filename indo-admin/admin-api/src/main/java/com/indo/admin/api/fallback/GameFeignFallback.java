package com.indo.admin.api.fallback;

import com.indo.admin.api.GameFeignClient;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class GameFeignFallback implements FallbackFactory<GameFeignClient> {

    @Override
    public GameFeignClient create(Throwable throwable) {
        throwable.printStackTrace();
        String msg = throwable == null ? "" : throwable.getMessage();
        if (!StringUtils.isEmpty(msg)) {
            log.error(msg);
        }
        return new GameFeignClient() {

            @Override
            public Result gamelogout(String account) {
                return Result.failed(ResultCode.DEGRADATION);
            }
        };
    }
}
