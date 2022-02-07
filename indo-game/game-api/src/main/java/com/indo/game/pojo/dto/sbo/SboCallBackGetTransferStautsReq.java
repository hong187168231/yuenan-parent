package com.indo.game.pojo.dto.sbo;

import lombok.Data;

@Data
public class SboCallBackGetTransferStautsReq extends SboCallBackParentReq{
    private String transferRefno;
}
