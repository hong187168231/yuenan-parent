package com.indo.game.pojo.dto.saba;

import lombok.Data;

@Data
public class SabaCallBackReq<T> {
    private String key;
    private String message;
}
