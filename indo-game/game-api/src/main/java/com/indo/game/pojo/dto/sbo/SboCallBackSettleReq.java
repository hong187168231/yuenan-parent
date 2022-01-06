package com.indo.game.pojo.dto.sbo;

import lombok.Data;

@Data
public class SboCallBackSettleReq extends SboCallBackParentReq{
    private String transferCode;
    private String winloss;//输赢多少金额。其中包括会员的投注金额。
    private int resultType;//赌注的结果 : {赢:0,输:1,平手:2}
    private String resultTime;
}
