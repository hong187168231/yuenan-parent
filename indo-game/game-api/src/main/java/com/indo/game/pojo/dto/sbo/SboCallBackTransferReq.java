package com.indo.game.pojo.dto.sbo;

import lombok.Data;

@Data
public class SboCallBackTransferReq extends SboCallBackParentReq{
    private String transferRefno;
//    131 : 会员将余额从我司系统转出。理解为：将余额从我司系统转入
//    130 : 会员将余额转入至我司系统。理解为：将余额从我司系统转出
    private String transferType;
    private String amount;
    private String gpid;
    private String transferTime;
}
