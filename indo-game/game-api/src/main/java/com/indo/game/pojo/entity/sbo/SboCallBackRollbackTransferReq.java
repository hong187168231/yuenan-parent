package com.indo.game.pojo.entity.sbo;

import lombok.Data;

@Data
public class SboCallBackRollbackTransferReq extends SboCallBackParentReq{
    private String transferRefno;
    private String gpid;
}
