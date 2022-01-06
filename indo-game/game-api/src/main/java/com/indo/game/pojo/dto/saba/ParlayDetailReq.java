package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ParlayDetailReq<T> {
    private long refNo;// Y (long) parlay_ref_no from GetBetDetail
    private BigDecimal stake;// Y (decimal) 注单金额
    private BigDecimal odds;// Y (decimal) 例如： 0.53
    private BigDecimal transOdds;// Y (decimal) 例如： 0.53
    private String ticketStatus;// Y (string) 交易结果 lose/won/lose/draw/void/refund/reject
    private String winlostDate;// Y (string) 决胜时间(仅显示日期) (yyyy-MM-dd 00:00:00.000) GMT-4
    private BigDecimal winlostAmount;// Y (decimal) 注单结算的金额
    private List<T> systemParlayDetail;// Y Json 格式: 请参阅下方说明

}
