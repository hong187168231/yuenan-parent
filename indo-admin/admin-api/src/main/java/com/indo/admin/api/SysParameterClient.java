package com.indo.admin.api;

import com.indo.admin.api.fallback.SysParameterFeignFallback;
import com.indo.admin.pojo.entity.SysParameter;
import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceIdConstant.ADMIN_SERVICE_ID, fallback = SysParameterFeignFallback.class)
public interface SysParameterClient {

    @GetMapping("/rpc/sysparam")
    Result<SysParameter> getByParamCode(@RequestParam("paramCode") String paramCode);
}
