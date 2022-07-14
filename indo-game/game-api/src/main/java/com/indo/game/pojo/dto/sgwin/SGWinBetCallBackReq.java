package com.indo.game.pojo.dto.sgwin;

import com.alibaba.fastjson.annotation.JSONField;

import com.indo.game.pojo.dto.cq.CqCallBackParentReq;
import lombok.Data;

@Data
public class SGWinBetCallBackReq extends CqCallBackParentReq {


    /**
     * 遊戲平台
     */
    @JSONField(name = "platform")
    private String platform;

    /**
     * 事件時間 格式為 RFC3339 如 2017-01-19T22:56:30-04:00
     */
    @JSONField(name = "eventTime")
    private String eventTime;

    /**
     * 贏得金額
     */
    @JSONField(name = "win")
    private Double win;

    /**
     * 下注金額
     */
    @JSONField(name = "bet")
    private Double bet;

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
