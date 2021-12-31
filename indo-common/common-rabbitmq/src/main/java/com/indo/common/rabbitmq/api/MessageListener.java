package com.indo.common.rabbitmq.api;

public interface MessageListener {

    void onMessage(Message message);
}