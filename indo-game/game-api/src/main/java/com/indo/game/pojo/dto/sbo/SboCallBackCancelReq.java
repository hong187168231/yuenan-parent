package com.indo.game.pojo.dto.sbo;

import lombok.Data;

@Data
public class SboCallBackCancelReq extends SboCallBackParentReq{
    private String transferCode;
    private String IsCancelAll;
    private String transactionId;
}
