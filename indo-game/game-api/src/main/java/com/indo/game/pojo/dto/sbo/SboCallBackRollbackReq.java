package com.indo.game.pojo.dto.sbo;

import lombok.Data;

@Data
public class SboCallBackRollbackReq extends SboCallBackParentReq{
    private String transferCode;
}
