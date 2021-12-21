package com.indo.game.pojo.vo.callback.sbo;

import lombok.Data;

@Data
public class SboCallBackGetBetStatusResp extends SboCallBackParentResp{
    private String transferCode;
    private String transactionId;
    private String status;
    private String winloss;//输赢多少金额。其中包括会员的投注金额。
    private String stake;//会员的投注金额

}
