package com.live.admin.api.fallback;

import com.live.admin.api.SysParameterClient;
import com.live.admin.api.UserFeignClient;
import com.live.admin.pojo.entity.SysParameter;
import com.live.admin.pojo.entity.SysUser;
import com.live.common.result.Result;
import com.live.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author puff
 * @createTime 2021/4/24 21:30
 */
@Component
@Slf4j
public class SysParameterFeignFallback implements SysParameterClient {

    @Override
    public Result<SysParameter> getByParamCode(String paramCode) {
        log.error("feign getByParamCode 远程调用失败");
        return Result.failed("调用失败");
    }
}
