package com.indo.admin.api;

import com.indo.admin.api.fallback.UserFeignFallbackClient;
import com.indo.admin.pojo.entity.SysUser;
import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = ServiceIdConstant.ADMIN_SERVICE_ID, fallbackFactory = UserFeignFallbackClient.class)
public interface UserFeignClient {

    @GetMapping("/api/v1/users/username/{username}")
    Result<SysUser> getUserByUsername(@PathVariable String username);
}
