package com.indo.game.pojo.dto.ug;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class UgCallBackTransactionItemReq {
    private String userId;//	string	50	Y	玩家帳號
    private BigDecimal amount;//	double		Y	金額
    private String ticketId;//	string	15	Y	注單 ID
    private Integer txnId;//	number		Y	交易編號
    private boolean bet;//	boolean		Y	此交易是否為投注
    private String marketCategory;//	string	20	Y	盤口類型
}
