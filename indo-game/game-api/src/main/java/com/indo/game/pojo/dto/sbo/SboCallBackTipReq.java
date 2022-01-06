package com.indo.game.pojo.dto.sbo;

import lombok.Data;

@Data
public class SboCallBackTipReq extends SboCallBackParentReq{
    private String transferCode;
    private String tipTime;
    private String amount;//下注金额
}
