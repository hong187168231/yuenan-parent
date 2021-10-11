package com.live.admin.api;

import com.live.admin.api.fallback.SysParameterFeignFallback;
import com.live.admin.api.fallback.UserFeignFallbackClient;
import com.live.admin.pojo.entity.SysParameter;
import com.live.admin.pojo.entity.SysUser;
import com.live.common.constant.ServiceIdConstant;
import com.live.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceIdConstant.ADMIN_SERVICE_ID, fallback = SysParameterFeignFallback.class)
public interface SysParameterClient {

    @GetMapping("/rpc/sysparam")
    Result<SysParameter> getByParamCode(@RequestParam("paramCode") String paramCode);
}
