package com.indo.game.pojo.dto.yl;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class YlCallBackReq {
    /**
     * key
     */
    @JSONField(name = "key")
    private String key;
    /**
     * message
     */
    @JSONField(name = "message")
    private String message;


}
