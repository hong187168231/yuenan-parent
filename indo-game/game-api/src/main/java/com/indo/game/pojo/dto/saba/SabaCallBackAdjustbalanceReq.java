package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SabaCallBackAdjustbalanceReq<T> extends SabaCallBackParentReq{
    private String operationId;// 交易纪录 id
    private String time;//  Y (string) 呼叫 AdjustBalance 的时间
    private String txId;//  Y (long) 沙巴体育系统交易 id.
    private String refId;//  Y (string) 唯一 id.
    private String betType;//  Y (int) 例如：1, 3
    private String betTypeName;//  Y (string) 只提供英文语系。
    private String winlostDate;//  Y (string) 决胜时间(仅显示日期)
//            (yyyy-MM-dd 00:00:00.000) GMT-4
    private String balanceInfo;//  Y 请参阅表 BalanceInfo
}
