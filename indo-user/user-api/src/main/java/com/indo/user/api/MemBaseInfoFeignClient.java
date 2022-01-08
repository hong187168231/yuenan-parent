package com.indo.user.api;


import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import com.indo.common.web.exception.KeepErrMsgConfiguration;
import com.indo.user.api.fallback.MemBaseInfoFeignFallback;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = ServiceIdConstant.USER_SERVICE_ID, contextId = "oauth-client",
        fallbackFactory = MemBaseInfoFeignFallback.class, configuration = {KeepErrMsgConfiguration.class})
public interface MemBaseInfoFeignClient {

    @GetMapping("/rpc/memBaseInfo/getMemBaseInfo/{userId}")
    Result<MemBaseinfo> getMemBaseInfo(@PathVariable Long userId);


    @GetMapping("/rpc/memBaseInfo/getByAccount/{account}")
    Result<MemBaseinfo> getByAccount(@PathVariable String account);

}
