package com.indo.game.pojo.entity.sbo;

import lombok.Data;

@Data
public class SboCallBackBonusReq extends SboCallBackParentReq{
    private String transferCode;
    private String transactionId;
    private String bonusTime;
    private String amount;//下注金额
}
