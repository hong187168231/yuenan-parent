package com.indo.game.pojo.entity.sbo;

import lombok.Data;

@Data
public class SboCallBackGetBetStatusReq extends SboCallBackParentReq{
    private String transferCode;
    private String transactionId;
}
