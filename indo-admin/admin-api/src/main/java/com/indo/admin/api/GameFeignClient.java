package com.indo.admin.api;


import com.indo.admin.api.fallback.GameFeignFallback;
import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import com.indo.common.web.exception.KeepErrMsgConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceIdConstant.GAME_SERVICE_ID, contextId = "oauth-client",
        fallbackFactory = GameFeignFallback.class, configuration = {KeepErrMsgConfiguration.class})
public interface GameFeignClient {


    @PostMapping("/api/v1/games/platform/gameLogout")
    Result gamelogout(@RequestParam String account);

}
