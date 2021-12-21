package com.indo.game.pojo.entity.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConfirmBetParlayTicketInfoReq {
    private String refId;// Y (string) 唯一 id.
    private long txId;// Y (long) 沙巴体育系统交易 id.
    private String licenseeTxId;// Y (string) 厂商系统交易 id
    private BigDecimal odds;// Y (decimal) 例如：-0.75, 0.96
    private BigDecimal actualAmount;// Y (decimal) 实际注单金额
    private boolean isOddsChanged;// Y (boolean) 例如：true, false
    private BigDecimal creditAmount;// Y (decimal) 需增加在玩家的金额。
    private BigDecimal debitAmount;// Y (decimal) 需从玩家扣除的金额。
    private String winlostDate;// Y (string) 决胜时间(仅显示日期)(yyyy-MM-dd 00:00:00.000) GMT-4
}
