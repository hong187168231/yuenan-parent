package com.indo.game.pojo.entity.ug;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UgCallBackTransactionItemReq {
    private boolean Bet;//此交易是否是投注
    private String Account;//登录帐号
    private BigDecimal Amount;//金额
    private String TransactionNo;//交易号
    private String BetID;//注单编号
}
