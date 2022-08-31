package com.indo.admin.api;

import com.indo.admin.api.fallback.SysIpLimitFeignFallback;
import com.indo.core.pojo.entity.SysIpLimit;
import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ServiceIdConstant.ADMIN_SERVICE_ID, fallback = SysIpLimitFeignFallback.class)
public interface SysIpLimitClient {
    @GetMapping("/rpc/sysIpLimit/findSysIpLimitByType")
    Result<List<SysIpLimit>>findSysIpLimitByType(@RequestParam("types") Integer types);
}
