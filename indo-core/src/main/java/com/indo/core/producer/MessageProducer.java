package com.indo.core.producer;

import com.indo.common.rabbitmq.bo.Message;
import com.indo.common.rabbitmq.exc.MessageRunTimeException;

import java.util.List;

public interface MessageProducer {

    void send(Message message) throws MessageRunTimeException;

    void send(List<Message> messages) throws MessageRunTimeException;
}
