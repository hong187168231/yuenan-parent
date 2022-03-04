package com.indo.game.pojo.dto.ae;


import com.alibaba.fastjson.annotation.JSONField;
import com.indo.game.pojo.dto.sbo.SboCallBackParentReq;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class AeCallBackTransferReq extends AeCallBackParentReq {
    /**
     * 转账交易代码
     */
    @JSONField(name = "txnId")
    private String txnId;
    /**
     * 1、当txnTypeId = 200(Adjust) 会传此栏位, 用于调帐时来自原始txnId, 2、txnTypeId = 100(Bet) 及 txnId 不等于
     * sourceTxndId时为免费游戏进入点
     */
    @JSONField(name = "sourceTxnId")
    private String sourceTxnId;
    /**
     * 游戏代码
     */
    @JSONField(name = "gameId")
    private String gameId;
    /**
     * 投注金额 (仅当txnTypeId = 100 (Bet)会传此栏位)
     */
    @JSONField(name = "betAmount")
    private Double betAmount;
    /**
     * 中奖金额 (仅当txnTypeId = 100 (Bet)会传此栏位)
     */
    @JSONField(name = "winAmount")
    private Double winAmount;
    /**
     * 转账金额，支持 2 位小数 (负数为扣款、正数为存款)
     */
    @JSONField(name = "amount")
    private Double amount;
    /**
     * 投注时间
     */
    @JSONField(name = "betTime")
    private Date betTime;
    /**
     * Bet(100), Adjust(200), LuckyDraw(300), Tournament(400)
     */
    @JSONField(name = "txnTypeId")
    private int txnTypeId;



}
