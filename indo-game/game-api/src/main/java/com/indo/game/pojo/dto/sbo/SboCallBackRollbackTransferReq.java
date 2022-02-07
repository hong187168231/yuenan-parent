package com.indo.game.pojo.dto.sbo;

import lombok.Data;

@Data
public class SboCallBackRollbackTransferReq extends SboCallBackParentReq{
    private String transferRefno;
}
