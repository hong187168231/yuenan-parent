package com.live.admin.api;

import com.live.admin.pojo.entity.SysOauthClient;
import com.live.common.constant.ServiceIdConstant;
import com.live.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = ServiceIdConstant.ADMIN_SERVICE_ID, contextId = "oauth-client")
public interface OAuthClientFeignClient {

    @GetMapping("/api/v1/oauth-clients/{clientId}")
    Result<SysOauthClient> getOAuthClientById(@PathVariable String clientId);
}
