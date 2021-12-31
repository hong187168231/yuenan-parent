package com.indo.common.rabbitmq.producer.brokery;


import com.indo.common.rabbitmq.api.Message;

public interface RabbitBroker {

    void rapidSend(Message message);
    void confirmSend(Message message);
    void reliantSend(Message message);
    void sendMessages();
}
