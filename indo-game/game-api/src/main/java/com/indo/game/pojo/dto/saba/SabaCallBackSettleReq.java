package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.util.List;

@Data
public class SabaCallBackSettleReq<T>{
    private String operationId;// 交易纪录 id
    private String action;
    private List<T> txns;

}
