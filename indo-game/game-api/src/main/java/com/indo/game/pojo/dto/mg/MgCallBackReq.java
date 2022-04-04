package com.indo.game.pojo.dto.mg;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MgCallBackReq {


    /**
     * 玩家的系統識別碼
     */
    @JSONField(name = "playerId")
    private String playerId;

    /**
     * PGSoft 与运营商之间共享密码
     */
    @JSONField(name = "txnType")
    private String txnType;

    /**
     * 交易類型
     */
    @JSONField(name = "txnEventType")
    private String txnEventType;



    /**
     * 交易金額，注意：總數可能為 0，但不可為負值
     */
    @JSONField(name = "amount")
    private BigDecimal amount;
    /**
     * 玩家幣別
     */
    @JSONField(name = "currency")
    private String currency;
    /**
     * MGPlus 交易識別碼，
     */
    @JSONField(name = "txnId")
    private String txnId;
    /**
     * 創建交易的時間戳記
     */
    @JSONField(name = "creationTime")
    private String creationTime;
    /**
     * 該交易的對應投注代碼
     */
    @JSONField(name = "betId")
    private String betId;
    /**
     * 該交易的對應回合代碼
     */
    @JSONField(name = "roundId")
    private String roundId;
    /**
     * 標示是否為此遊戲回合的最終交易
     */
    @JSONField(name = "completed")
    private boolean completed;

}
