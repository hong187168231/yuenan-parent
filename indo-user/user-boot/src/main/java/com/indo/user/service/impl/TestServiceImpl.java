package com.indo.user.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import com.indo.user.service.TestService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service
public class TestServiceImpl implements TestService {


    /**
     * 限流降级
     *
     * @return
     */
    @Override
    @SentinelResource(value = "sayHello", blockHandler = "sayHelloExceptionHandler")
    public String sayHello(String name, HttpServletRequest request) {
        return "hello," + name;
    }

    /**
     * 熔断降级
     *
     * @return
     */
    @Override
    @SentinelResource(value = "circuitBreaker", fallback = "circuitBreakerFallback", blockHandler = "sayHelloExceptionHandler")
    public String circuitBreaker(String name, HttpServletRequest request) {
        if ("zhangsan".equals(name)) {
            return "hello," + name;
        }
        String countryCode = request.getHeader("countryCode");
        throw new BizException(MessageUtils.get("u170035", countryCode));
    }



}
