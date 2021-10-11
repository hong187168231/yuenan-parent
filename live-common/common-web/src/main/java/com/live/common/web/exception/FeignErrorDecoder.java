package com.live.common.web.exception;

import com.alibaba.fastjson.JSONObject;
import com.live.common.result.Result;
import com.live.common.result.ResultCode;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        BizException baseException = null;
        try {
            String errorContent = Util.toString(response.body().asReader());
            Result result = JSONObject.parseObject(errorContent, Result.class);
            baseException = new BizException(ResultCode.getValue(result.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseException;
    }
}
