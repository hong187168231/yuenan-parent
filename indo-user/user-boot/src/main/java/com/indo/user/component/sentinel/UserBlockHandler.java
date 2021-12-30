package com.indo.user.component.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.indo.admin.pojo.vo.UserVO;
import com.indo.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户接口降级逻辑
 *
 * @author puff
 * @createTime 2021/4/23 23:30
 */
@Slf4j
//@Component
public class UserBlockHandler {

    /**
     * 获取当前登录用户信息的熔断降级处理
     *
     * @param blockException
     * @return
     */
    public static Result<UserVO> handleGetCurrentUserBlock(BlockException blockException) {
        return Result.success(new UserVO());
    }


    public static Result handleGetUserByUsernameBlock(String username, BlockException blockException) {
        log.info("降级了：{}", username);
        return Result.failed("降级 了");
    }


    public static Result circuitBreakerFallback(String name) {
        return Result.success("服务异常，熔断降级, 请稍后重试呀!");
    }

    public static Result sayHelloExceptionHandler(String name, BlockException ex) {
        return Result.failed("访问过快，限流降级, 请稍后重试呀!");
    }
}
