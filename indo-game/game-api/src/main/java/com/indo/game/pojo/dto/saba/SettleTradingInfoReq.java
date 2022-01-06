package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SettleTradingInfoReq<T> {
    private String userId;//  Y (string) 用户 id.
    private String refId;//  Y (string) 唯一 id.
    private long txId;//  Y (long) 沙巴系统交易 id
    private String updateTime;//  Y 更新时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private String winlostDate;//  Y 决胜时间(仅显示日期)(yyyy-MM-dd 00:00:00.000) GMT-4
    private String status;//  Y (string) 交易结果  lose/won/lose/draw/void/refund/reject
    private BigDecimal payout;//  Y (decimal) 注单赢回的金额
    private BigDecimal creditAmount;//  Y (decimal) 需增加在玩家的金额。
    private BigDecimal debitAmount;//  Y (decimal) 需从玩家扣除的金额。
    private List<T> parlayDetail;//  Y Json 格式: 请参阅下方说明

}
