package com.indo.admin.api;

import com.indo.admin.api.fallback.MsgFeignFallback;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.entity.MsgPushRecord;
import com.indo.admin.pojo.entity.MsgStationLetter;
import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = ServiceIdConstant.ADMIN_SERVICE_ID, fallback = MsgFeignFallback.class)
public interface MsgFeignClient {

    @PostMapping("/rpc/msg/personal")
    Result<List<MsgStationLetter>> getPersonalMsg(MsgDTO msgDTO);

    @PostMapping("/rpc/msg/sys")
    Result<List<MsgPushRecord>> getSysMsg(MsgDTO msgDTO);


}
