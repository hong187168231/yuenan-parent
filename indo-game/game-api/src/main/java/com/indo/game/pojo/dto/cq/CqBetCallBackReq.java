package com.indo.game.pojo.dto.cq;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class CqBetCallBackReq extends CqCallBackParentReq {


    /**
     * 遊戲平台
     */
    @JSONField(name = "platform")
    private String platform;

    /**
     * 事件時間 格式為 RFC3339
     * 如 2017-01-19T22:56:30-04:00
     */
    @JSONField(name = "eventTime")
    private String eventTime;


}
