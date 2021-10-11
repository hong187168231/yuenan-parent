package com.live.admin.api;

import com.live.admin.api.fallback.UserFeignFallbackClient;
import com.live.admin.pojo.entity.SysUser;
import com.live.common.constant.ServiceIdConstant;
import com.live.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = ServiceIdConstant.ADMIN_SERVICE_ID, fallback = UserFeignFallbackClient.class)
public interface UserFeignClient {

    @GetMapping("/api/v1/users/username/{username}")
    Result<SysUser> getUserByUsername(@PathVariable String username);
}
