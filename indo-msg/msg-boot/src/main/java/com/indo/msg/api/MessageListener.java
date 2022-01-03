package com.indo.msg.api;

import com.indo.common.rabbitmq.bo.Message;

public interface MessageListener {

    void onMessage(Message message);
}