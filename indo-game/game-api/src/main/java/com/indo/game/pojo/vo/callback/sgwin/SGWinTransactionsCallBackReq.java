package com.indo.game.pojo.vo.callback.sgwin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SGWinTransactionsCallBackReq {

    @JSONField(name = "MemberId")
    private String MemberId;//	String 	member1	ID of the member
    @JSONField(name = "Currency")
    private String Currency;//	String	CNY	Currency Code
    @JSONField(name = "Amount")
    private BigDecimal Amount;//	Decimal	10.00	Debit amount
    @JSONField(name = "TransactionTime")
    private String TransactionTime;//	DateTime	2019-07-12 07:13:02 +08:00	Date time of the bet with the time zone.
    @JSONField(name = "TransactionId")
    private String TransactionId;//	String	TLD190711529679190	Unique Identifier for each api call
    @JSONField(name = "RefTransactionId")
    private String RefTransactionId;//	String	TLD190711529679189	TransactionId of the need to refund
    @JSONField(name = "betGroupId")
    private String betGroupId;//	String	2019071911100332	Group ID of Bets

}
