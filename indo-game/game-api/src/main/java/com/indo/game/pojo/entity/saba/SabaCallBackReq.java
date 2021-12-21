package com.indo.game.pojo.entity.saba;

import lombok.Data;

@Data
public class SabaCallBackReq<T> {
    private String key;
    private T message;
}
