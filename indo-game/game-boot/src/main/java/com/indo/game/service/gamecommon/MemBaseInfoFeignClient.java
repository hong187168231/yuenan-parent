package com.indo.game.service.gamecommon;

import com.indo.admin.api.fallback.UserFeignFallbackClient;
import com.indo.admin.pojo.entity.SysUser;
import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = ServiceIdConstant.USER_SERVICE_ID, fallback = UserFeignFallbackClient.class)
public interface MemBaseInfoFeignClient {

    @GetMapping("/membaseinfo/getMemBaseInfoById")
    MemBaseinfo getMemBaseInfoById(int id);
}
