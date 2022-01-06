package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.util.List;

@Data
public class SabaCallBackCashOutReq<T> extends SabaCallBackParentReq{
    private String operationId;// 交易纪录 id
    private List<T> txns;//  Y (array) 请参阅表 TicketDetail

}
