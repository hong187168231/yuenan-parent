package com.indo.user.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.indo.user.service.TestService;
import org.springframework.stereotype.Service;


@Service
public class TestServiceImpl implements TestService {


    /**
     * 限流降级
     *
     * @return
     */
    @SentinelResource(value = "sayHello", blockHandler = "sayHelloExceptionHandler")
    public String sayHello(String name) {
        return "hello," + name;
    }

    /**
     * 熔断降级
     *
     * @return
     */
    @SentinelResource(value = "circuitBreaker", fallback = "circuitBreakerFallback", blockHandler = "sayHelloExceptionHandler")
    public String circuitBreaker(String name) {
        if ("zhangsan".equals(name)) {
            return "hello," + name;
        }
        throw new RuntimeException("发生异常");
    }



}
