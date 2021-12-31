package com.indo.common.rabbitmq;

import com.google.common.base.Preconditions;
import com.indo.common.rabbitmq.api.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.messaging.converter.MessageConversionException;

public class RabbitMessageConverter implements MessageConverter {


    private GenericMessageConverter delegate;

//  private final String delaultExprie = String.valueOf(24 * 60 * 60 * 1000);
    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.delegate = genericMessageConverter;
    }

    @Override
    public org.springframework.amqp.core.Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
//      messageProperties.setExpiration(delaultExprie);
        Message message = (Message) object;
        messageProperties.setDelay(message.getDelayMills());
        return this.delegate.toMessage(object, messageProperties);
    }

    @Override
    public Object fromMessage(org.springframework.amqp.core.Message message) throws MessageConversionException {
        Message msg = (Message) this.delegate.fromMessage(message);
        return msg;
    }

}
