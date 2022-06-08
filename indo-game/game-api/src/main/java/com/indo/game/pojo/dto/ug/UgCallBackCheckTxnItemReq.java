package com.indo.game.pojo.dto.ug;


import lombok.Data;

@Data
public class UgCallBackCheckTxnItemReq {
    private String userId;//	string	50	Y	玩家帳號
    private String txnId;//	number		Y	交易編號
}
