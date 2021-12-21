package com.indo.game.pojo.entity.sbo;

import lombok.Data;

@Data
public class SboCallBackDeductReq extends SboCallBackParentReq{
    private String transferCode;
    private String transactionId;
    private String betTime;
    private String amount;//下注金额
}
