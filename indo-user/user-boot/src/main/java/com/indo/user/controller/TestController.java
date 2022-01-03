package com.indo.user.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.indo.admin.api.SysParameterClient;
import com.indo.admin.api.UserFeignClient;
import com.indo.admin.pojo.entity.SysParameter;
import com.indo.admin.pojo.entity.SysUser;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.constant.RabbitConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.rabbitmq.bo.Message;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.msg.api.MessageProducer;
import com.indo.user.pojo.dto.TestDTO;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Api(tags = "会员接口")
@RestController
@RequestMapping("/test")
@Slf4j
@AllArgsConstructor
public class TestController {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private SysParameterClient sysParameterClient;

    @Autowired
    private TestService testService;
    @Autowired
    private MessageProducer messageProducer;

    @GetMapping("/t1")
    @AllowAccess
    public String hello(@RequestParam("name") String name) {
        return testService.sayHello(name);
    }

    @GetMapping("/t2")
    @AllowAccess
    public String circuitBreaker(@RequestParam("name") String name) {
        return testService.circuitBreaker(name);
    }

    @ApiOperation(value = "hello")
    @GetMapping("/hello")
    @AllowAccess
    public String detail(@LoginUser LoginInfo loginInfo) {
        MemBaseinfo memBaseInfo = new MemBaseinfo();
        memBaseInfo.setAccount("dd");
        memBaseInfo.setBalance(new BigDecimal(2000));
        redisUtils.set("dsd", memBaseInfo, 60 * 60 * 24 * 7);
        return "ok";
    }

    @GetMapping("/rabbitTest")
    @AllowAccess
    public String rabbitTest() {
        Message message = new Message();
        message.setRoutingKey("key.user");
        message.setTopic(RabbitConstants.USER_EXCHANGE_TOPIC);
        messageProducer.send(message);
        return "ok";
    }


    @AllowAccess
    @ApiOperation(value = "hello2")
    @GetMapping("/hello2")
    public Object helloTwo() {
        Result<SysParameter> parameter = sysParameterClient.getByParamCode("test11");
        return JSON.toJSONString(parameter);
    }


    @AllowAccess
    @PostMapping("/testValidated")
    public Result Validated(@Validated @RequestBody TestDTO testDTO) {
        return Result.success(testDTO);
    }


}
