package com.indo.common.rabbitmq.producer.brokery;

import com.google.common.base.Preconditions;
import com.indo.common.rabbitmq.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProducerClient implements MessageProducer {

    @Autowired
    RabbitBroker rabbitBroker;

    @Override
    public void send(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        String messageType = message.getMessageType();
        switch (messageType){
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
    public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {

    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {

    }
}
