package com.indo.game.pojo.dto.sbo;

import lombok.Data;

@Data
public class SboCallBackGetBetStatusReq extends SboCallBackParentReq{
    private String transferCode;
    private String transactionId;
}
