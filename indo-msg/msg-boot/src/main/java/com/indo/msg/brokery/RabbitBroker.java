package com.indo.msg.brokery;


import com.indo.common.rabbitmq.bo.Message;

public interface RabbitBroker {

    void rapidSend(Message message);

    void confirmSend(Message message);

    void reliantSend(Message message);

    void sendMessages();
}
