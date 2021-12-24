package com.indo.game.pojo.entity.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConfirmBet3rdTicketInfoReq {
    private String refId;// Y (string) 唯一 id.
    private long txId;// Y  沙巴系统交易 id
    private String licenseeTxId;// Y (string) 厂商系统交易 id
    private BigDecimal creditAmount;// Y (decimal) 需增加在玩家的金额。
    private BigDecimal debitAmount;// Y (decimal) 需从玩家扣除的金额。
    private String winlostDate;// Y (string) 决胜时间(仅显示日期)(yyyy-MM-dd 00:00:00.000) GMT-4
}
