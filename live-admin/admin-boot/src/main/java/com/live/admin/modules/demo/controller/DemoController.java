//package com.live.admin.modules.demo.controller;
//
//import com.live.admin.pojo.entity.MemLevel;
//import com.live.admin.producer.RabbitmqSender;
//import com.live.common.constant.RabbitConstants;
//import com.live.common.result.Result;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Api(tags = "demo接口")
//@RestController
//@RequestMapping("/api/v1/demo")
//@Slf4j
//@AllArgsConstructor
//public class DemoController {
//
//    @Resource
//    private RabbitmqSender rabbitmqSender;
//
//    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
//
//
//
//    @ApiOperation(value = "发送rabbit消息")
//    @GetMapping("/sendMsg1")
//    public Result add2(String msg) {
//        try {
//            Map<String, Object> properties = new HashMap<>();
//            properties.put("number", "12345");
//            properties.put("send_time", simpleDateFormat.format(new Date()));
//            MemLevel memLevel  = new MemLevel();
//            memLevel.setImage(msg);
//            rabbitmqSender.send1(memLevel,properties);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Result.success();
//    }
//
//
//    @ApiOperation(value = "发送rabbit消息")
//    @GetMapping("/sendMsg2")
//    public Result add(String msg) {
//        try {
//            rabbitmqSender.send2(msg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Result.success();
//    }
//
//
//    @ApiOperation(value = "发送rabbit消息")
//    @GetMapping("/sendMsg3")
//    public Result add3(String msg) {
//        try {
//            Map<String, Object> properties = new HashMap<>();
//            properties.put("number", "12345");
//            properties.put("send_time", simpleDateFormat.format(new Date()));
//            MemLevel memLevel  = new MemLevel();
//            memLevel.setImage(msg);
//            rabbitmqSender.send3(msg,properties);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Result.success();
//    }
//
//
//
//
//}
