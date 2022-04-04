package com.indo.admin.api.fallback;

import com.indo.admin.api.SysIpLimitClient;
import com.indo.admin.pojo.entity.SysIpLimit;
import com.indo.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SysIpLimitFeignFallback implements SysIpLimitClient {
    @Override
    public Result<List<SysIpLimit>> findSysIpLimitByType(Integer types) {
        log.error("feign findSysIpLimitByType 远程调用失败");
        return Result.failed("调用失败");
    }
}
