package com.indo.game.pojo.entity.awc;

import java.util.List;

public class AwcApiRequestParentData<T>{
    private String key;
    private T message;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
