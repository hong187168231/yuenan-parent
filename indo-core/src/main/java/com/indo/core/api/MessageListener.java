package com.indo.core.api;

import com.indo.common.rabbitmq.bo.Message;

public interface MessageListener {

    void onMessage(Message message);
}