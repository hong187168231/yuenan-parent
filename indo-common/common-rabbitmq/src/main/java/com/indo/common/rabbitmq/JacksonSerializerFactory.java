package com.indo.common.rabbitmq;


import com.indo.common.rabbitmq.ser.SerializerFactory;
import com.indo.common.rabbitmq.bo.Message;

public class JacksonSerializerFactory implements SerializerFactory {

    public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public JacksonSerializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }

}