package com.indo.game.pojo.dto.dg;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class DgCallBackReq<T> {


    /**
     * 遊戲平台
     */
    @JSONField(name = "token")
    private String token;

    /**
     *
     */
    @JSONField(name = "member")
    private T member;

    /**
     * 注单ID
     */
    @JSONField(name = "ticketId")
    private String ticketId;

    /**
     * 转账流水号
     */
    @JSONField(name = "data")
    private String data;

    /**
     * 有效投注
     */
    @JSONField(name = "validbet")
    private Double validbet;
    /**
     * 事件時間 格式為 RFC3339
     */
    @JSONField(name = "createTime")
    private String createTime;

    /**
     * 漁機遊戲(fish) or 牌桌遊戲(table)or 視訊遊戲(live)
     */
    @JSONField(name = "gametype")
    private String gametype;


}
