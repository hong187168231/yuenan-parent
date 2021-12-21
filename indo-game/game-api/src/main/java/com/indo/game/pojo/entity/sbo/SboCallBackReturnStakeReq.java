package com.indo.game.pojo.entity.sbo;

import lombok.Data;

@Data
public class SboCallBackReturnStakeReq extends SboCallBackParentReq{
    private String transferCode;
    private String transactionId;
    private String returnStakeTime;
    private String currentStake;//下注金额
}
