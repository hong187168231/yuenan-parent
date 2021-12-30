package com.indo.user.api;


import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import com.indo.common.web.exception.KeepErrMsgConfiguration;
import com.indo.user.api.fallback.MemBaseInfoFeignFallback;
import com.indo.user.api.fallback.MemGoldFeignFallback;
import com.indo.user.pojo.dto.MemGoldChangeDTO;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = ServiceIdConstant.USER_SERVICE_ID,
        fallbackFactory = MemGoldFeignFallback.class, configuration = {KeepErrMsgConfiguration.class})
public interface MemGoldFeignClient {

    @PutMapping(value = "/rpc/memBaseInfo/updateMemGoldChange", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result<Boolean> updateMemGoldChange(MemGoldChangeDTO memGoldChangeDTO);

}
