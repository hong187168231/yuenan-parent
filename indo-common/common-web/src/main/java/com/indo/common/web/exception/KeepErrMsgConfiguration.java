package com.indo.common.web.exception;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class KeepErrMsgConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    public class FeignErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            Exception exception = null;
            try {
                // 获取原始的返回内容
                String json = Util.toString(response.body().asReader());
                exception = new RuntimeException(json);
                // 将返回内容反序列化为Result，这里应根据自身项目作修改
                Result result = JSONObject.parseObject(json, Result.class);
                // 业务异常抛出简单的 RuntimeException，保留原来错误信息
                exception = new BizException(ResultCode.getValue(result.getCode()));
                log.error(result.getMsg());
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
            return exception;
        }

    }

}
