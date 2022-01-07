package com.indo.msg.autoconfigure;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.indo.msg.*"})
@MapperScan("com.indo.msg.mapper")
public class MsgProdcuerAutoConfigure {


}
