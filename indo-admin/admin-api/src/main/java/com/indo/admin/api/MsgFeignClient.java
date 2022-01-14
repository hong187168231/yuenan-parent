package com.indo.admin.api;

import com.indo.admin.api.fallback.MsgFeignFallback;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.MsgPushRecordVO;
import com.indo.admin.pojo.vo.MsgStationLetterVO;
import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceIdConstant.ADMIN_SERVICE_ID, fallback = MsgFeignFallback.class)
public interface MsgFeignClient {

    @PostMapping("/rpc/msg/personal")
    Result<List<MsgStationLetterVO>> getPersonalMsg(MsgDTO msgDTO);

    @PostMapping("/rpc/msg/sys")
    Result<List<MsgPushRecordVO>> getSysMsg(MsgDTO msgDTO);


}
