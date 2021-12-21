package com.indo.game.pojo.entity.sbo;

import lombok.Data;

@Data
public class SboCallBackGetTransferStautsReq extends SboCallBackParentReq{
    private String transferRefno;
    private String gpid;
}
