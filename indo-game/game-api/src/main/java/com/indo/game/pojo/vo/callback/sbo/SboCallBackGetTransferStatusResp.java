package com.indo.game.pojo.vo.callback.sbo;

import lombok.Data;

@Data
public class SboCallBackGetTransferStatusResp extends SboCallBackParentResp{
    private String accountName;
    private String balance;
//    0 : TransferNotExists, 转帐交易不存在。
//    1 : Processing, 转帐交易仍在进行中。
//    2 : Transferred, 转帐交易已经完成。
    private String transferStatus;
    private String amount;//会员转帐之金额
}
