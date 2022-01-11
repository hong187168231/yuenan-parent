package com.indo.admin.api.fallback;

import com.indo.admin.api.MsgFeignClient;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.entity.MsgPushRecord;
import com.indo.admin.pojo.entity.MsgStationLetter;
import com.indo.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author puff
 * @createTime 2021/4/24 21:30
 */
@Component
@Slf4j
public class MsgFeignFallback implements MsgFeignClient {


    @Override
    public Result<List<MsgStationLetter>> getPersonalMsg(MsgDTO msgDTO) {
        log.error("feign getByParamCode 远程调用失败");
        return Result.failed("调用失败");
    }

    @Override
    public Result<List<MsgPushRecord>> getSysMsg(MsgDTO msgDTO) {
        log.error("feign getByParamCode 远程调用失败");
        return Result.failed("调用失败");
    }
}
