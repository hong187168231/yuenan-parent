package com.indo.game.pojo.dto.cq;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CqSportsEventCallBackReq {

    /**
     * 混合碼
     */
    @JSONField(name = "mtcode")
    private String mtcode;

    /**
     * 金額
     */
    @JSONField(name = "amount")
    private BigDecimal amount;

    /**
     * 注單號
     */
    @JSONField(name = "roundid")
    private String roundid;

    /**
     * 事件時間
     */
    @JSONField(name = "eventtime")
    private String eventtime;

    /**
     * 遊戲編碼
     */
    @JSONField(name = "gamecode")
    private String gamecode;

    /**
     * 遊戲廠商代號
     */
    @JSONField(name = "gamehall")
    private String gamehall;

    /**
     * 有效投注
     */
    @JSONField(name = "validbet")
    private String validbet;

}
