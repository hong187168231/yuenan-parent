package com.indo.admin.api.fallback;

import com.indo.admin.api.FileFeignClient;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author puff
 * @createTime 2021/4/24 21:30
 */
@Component
@Slf4j
public class FileFeignFallback implements FileFeignClient {


    @Override
    public Result<String> upload(MultipartFile file) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return Result.failed(ResultCode.SYSTEM_EXECUTION_ERROR);
    }
}
