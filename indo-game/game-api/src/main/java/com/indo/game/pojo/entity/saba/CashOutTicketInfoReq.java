package com.indo.game.pojo.entity.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashOutTicketInfoReq {

    private String userId;// Y (string) 用户 id。
    private String refId;// Y (string) 唯一 id。
    private long txId;// Y (long) 在沙巴体育系统的 cashout(卖票)票号 id
    private String updateTime;// Y 更新时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private BigDecimal availableCashOutAmount;// Y (decimal) 可实时兑现的投注额。
    private BigDecimal cashOutActualAmount;// Y (decimal) 卖出实际投注额。
    private BigDecimal cashOutAmount;// Y (decimal) 卖出投注额。
    private BigDecimal buybackAmount;// Y (decimal) 注单卖出金额。
    private String TicketStatus;// Y (string) won/lose/draw/void/refund
    private OriginalTicket OriginalTicket;// Y 请参阅 Original Ticket Info
    private BigDecimal creditAmount;// Y (decimal) 需增加在玩家的金额。
    private BigDecimal debitAmount;// Y (decimal) 需从玩家扣除的金额。
}
