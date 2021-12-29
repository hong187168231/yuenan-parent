package com.indo.user.api.fallback;

import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.user.api.MemBaseInfoFeignClient;
import com.indo.user.pojo.dto.MemGoldChangeDTO;
import com.indo.user.pojo.entity.MemBaseinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author puff
 * @createTime 2021/4/24 21:30
 */
@Component
@Slf4j
public class MemBaseInfoFeignFallback implements MemBaseInfoFeignClient {

    @Override
    public Result<MemBaseinfo> getMemBaseInfo(Long userId) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return Result.failed(ResultCode.DEGRADATION);
    }

    @Override
    public Result<MemBaseinfo> getByAccount(String account) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return Result.failed(ResultCode.DEGRADATION);
    }

    @Override
    public Result<Boolean> updateMemGoldChange(MemGoldChangeDTO memGoldChangeDTO) {
        log.error("feign远程调用用户账表服务异常后的降级方法");
        return Result.failed(ResultCode.DEGRADATION);
    }


}
