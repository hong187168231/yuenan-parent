package com.indo.common.rabbitmq.api;

public interface SendCallback {

    void onSuccess();

    void onFailure();
}