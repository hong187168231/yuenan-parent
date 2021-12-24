package com.indo.game.pojo.entity.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBetTicketInfoReq {

    private String userId;// Y (string) 用户 id。
    private String refId;// Y (string) 唯一 id。
    private long txId;// Y (long) 在沙巴体育系统的 cashout(卖票)票号 id
    private String updateTime;// Y 更新时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private BigDecimal betAmount;// Y (decimal)  注单金额
    private BigDecimal actualAmount;// Y (decimal) 实际注单金额
    private short oddsType;// (short) 例如：1, 2, 3, 4, 5
    private BigDecimal odds;// Y (decimal)  例如：-0.95, 0.75
    private BigDecimal creditAmount;// Y (decimal) 需增加在玩家的金额。
    private BigDecimal debitAmount;// Y (decimal) 需从玩家扣除的金额。
}
