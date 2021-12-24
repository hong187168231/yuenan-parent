package com.indo.game.pojo.entity.saba;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SabaCallBackConfirmBetReq<T> extends SabaCallBackParentReq{
    private String operationId;// 交易纪录 id
    private String updateTime;// 更新时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private String transactionTime;// 沙巴系統投注交易时间

    private List<T> txns;

}