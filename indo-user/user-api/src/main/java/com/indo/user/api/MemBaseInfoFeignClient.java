package com.indo.user.api;


import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import com.indo.user.api.fallback.MemBaseInfoFeignFallback;
import com.indo.user.pojo.dto.MemGoldChangeDTO;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ServiceIdConstant.USER_SERVICE_ID, fallback = MemBaseInfoFeignFallback.class)
public interface MemBaseInfoFeignClient {

    @GetMapping("/rpc/memBaseInfo/getMemBaseInfo/{userId}")
    Result<MemBaseinfo> getMemBaseInfo(@PathVariable Long userId);


    @GetMapping("/rpc/memBaseInfo/getByAccount/{account}")
    Result<MemBaseinfo> getByAccount(@PathVariable String account);


    @PostMapping("/rpc/memBaseInfo/updateMemGoldChange")
    Result<Boolean> updateMemGoldChange( MemGoldChangeDTO memGoldChangeDTO);

}
