//package com.indo.user.api.fallback;
//
//import com.indo.common.result.Result;
//import com.indo.common.result.ResultCode;
//import com.indo.common.utils.StringUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.openfeign.FallbackFactory;
//
//@Slf4j
//public class MemGoldFeignFallback implements FallbackFactory<MemGoldFeignClient> {
//
//    public static final String ERR_MSG = "Test接口暂时不可用: ";
//
//    @Override
//    public MemGoldFeignClient create(Throwable throwable) {
//        throwable.printStackTrace();
//        String msg = throwable == null ? "" : throwable.getMessage();
//        if (!StringUtils.isEmpty(msg)) {
//            log.error(msg);
//        }
//        return memGoldChangeDTO -> {
//            System.out.println(ERR_MSG + msg);
//            return Result.failed(ResultCode.DEGRADATION);
//        };
//    }
//}
