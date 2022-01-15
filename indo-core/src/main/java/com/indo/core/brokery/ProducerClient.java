package com.indo.core.brokery;

import com.google.common.base.Preconditions;
import com.indo.common.rabbitmq.bo.Message;
import com.indo.common.rabbitmq.bo.MessageType;
import com.indo.common.rabbitmq.exc.MessageRunTimeException;
import com.indo.core.producer.MessageProducer;
import com.indo.core.mapper.BrokerMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProducerClient implements MessageProducer {

    @Autowired
    RabbitBroker rabbitBroker;

    @Autowired
    private BrokerMessageMapper brokerMessageMapper;

    @Override
    public void send(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        String messageType = message.getMessageType();
        switch (messageType) {
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {
        messages.forEach(message -> {
            message.setMessageType(MessageType.RAPID);
            MessageHolder.add(message);
        });
        rabbitBroker.sendMessages();

    }
}
