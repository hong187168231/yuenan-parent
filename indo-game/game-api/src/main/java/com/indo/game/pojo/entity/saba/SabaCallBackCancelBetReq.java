package com.indo.game.pojo.entity.saba;

import lombok.Data;

import java.util.List;

@Data
public class SabaCallBackCancelBetReq<T> extends SabaCallBackParentReq{
    private String operationId;// 交易纪录 id
    private String updateTime;// 更新时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4

    private List<T> txns;

}
