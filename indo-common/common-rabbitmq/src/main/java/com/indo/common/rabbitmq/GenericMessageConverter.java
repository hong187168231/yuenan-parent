package com.indo.common.rabbitmq;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.messaging.converter.MessageConversionException;

public class GenericMessageConverter implements MessageConverter {


    private Serializer serializer;

    public GenericMessageConverter(Serializer serializer) {
        Preconditions.checkNotNull(serializer);
        this.serializer = serializer;
    }

    /**
     * 使用序列化： org.springframework.amqp.core.Message 转为Object
     */
    @Override
    public Object fromMessage(org.springframework.amqp.core.Message message) throws MessageConversionException {
        return this.serializer.deserialize(message.getBody());
    }

    /**
     * 使用反序列化 将自己的object转换为org.springframework.amqp.core.Message
     */
    @Override
    public org.springframework.amqp.core.Message toMessage(Object object, MessageProperties messageProperties)
            throws MessageConversionException {
        return new org.springframework.amqp.core.Message(this.serializer.serializeRaw(object), messageProperties);
    }


}
