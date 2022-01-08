package com.indo.core.autoconfigure;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.indo.core.*"})
@MapperScan("com.indo.msg.mapper")
public class MsgProdcuerAutoConfigure {


}
