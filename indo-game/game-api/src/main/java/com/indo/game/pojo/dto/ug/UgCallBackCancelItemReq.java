package com.indo.game.pojo.dto.ug;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class UgCallBackCancelItemReq {
    private String userId;//	string	50	Y	玩家帳號
    private String ticketId;//	string	15	Y	注單 ID
    private String txnId;//	number		Y	交易編號
}
