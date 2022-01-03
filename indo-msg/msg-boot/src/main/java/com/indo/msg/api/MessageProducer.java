package com.indo.msg.api;

import com.indo.common.rabbitmq.bo.Message;
import com.indo.common.rabbitmq.exc.MessageRunTimeException;

import java.util.List;

public interface MessageProducer {

    void send(Message message) throws MessageRunTimeException;

    /**
     * 消息的发送，附带SendCallback回调执行响应的业务逻辑处理
     *
     * @param message
     * @throws MessageRunTimeException
     */
    void send(Message message, SendCallback sendCallback) throws MessageRunTimeException;

    void send(List<Message> messages) throws MessageRunTimeException;
}
