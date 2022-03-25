package com.indo.game.pojo.dto.jili;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JiliCallbackSessionBetReq extends JiliCallbackBetReq {
    private String userId;
    // 注单类型:
    //1=下注
    //2=结算
    private Integer type;

    private Long sessionId;
    // 返回金额
    private BigDecimal turnover;
    // --------------------以下非必填
    // 联合彩金押注贡献值
    private BigDecimal jpContribute;
    // 联合彩金派奖贡献值
    private BigDecimal jpWin;
    // 为 true 时表示此注单为离线开奖
    private Boolean isFreeRound;
    // 预扣金额
    private BigDecimal preserve;

}
