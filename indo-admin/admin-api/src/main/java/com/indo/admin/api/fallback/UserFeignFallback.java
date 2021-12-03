package com.indo.admin.api.fallback;

import com.indo.admin.api.UserFeignClient;
import com.indo.admin.pojo.entity.SysUser;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author puff
 * @createTime 2021/4/24 21:30
 */
@Component
@Slf4j
public class UserFeignFallback implements UserFeignClient {

    @Override
    public Result<SysUser> getUserByUsername(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return Result.failed(ResultCode.DEGRADATION);
    }
}
