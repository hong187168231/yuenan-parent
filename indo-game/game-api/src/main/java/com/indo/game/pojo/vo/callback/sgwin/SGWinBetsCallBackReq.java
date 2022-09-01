package com.indo.game.pojo.vo.callback.sgwin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SGWinBetsCallBackReq {

    @JSONField(name = "BrandCode")
    private String BrandCode;//	String	vntest	ID of sub company
    @JSONField(name = "Timestamp")
    private Long Timestamp;//	Long	1565837724000	Timestamp of placebet called.
    @JSONField(name = "Sign")
    private String Sign;//	String	M25XWSwHAkKfO9NTRiJ8RD8iKq00ZTpVp3+2gVTRQfRjFy6q6Hlku2qZnLwC7Ges0PCQg82s1d3I1CwKYbBqRg==	Sign will be generated at launch game URL and validate the debit call. Refer to appendix.
    @JSONField(name = "MemberId")
    private String MemberId;//	String 	member1	ID of the member
    @JSONField(name = "Lottery")
    private String Lottery;//	String	HK6	Lottery code
    @JSONField(name = "Currency")
    private String Currency;//	String	VND	Currency code
    @JSONField(name = "Amount")
    private BigDecimal Amount;// 	Decimal 	300.0	- Bet amount of the bet.
//- Amount should always > 0.
//            - The amount to be debited from the balance.
@JSONField(name = "TransactionTime")
private String TransactionTime;//	DateTime	2019-07-12 07:13:02 +08:00	Date time of the bet with the time zone.
    @JSONField(name = "TransactionId")
    private String TransactionId;//	String  	TLD190711529665573	Transaction Id of the debit. Unique ID for each debit call.
    @JSONField(name = "DrawNumber")
    private String DrawNumber;//	String	190721524013786	Number of each draw
    @JSONField(name = "BetGroupId")
    private String BetGroupId;//	String	2019071911100332	Group ID of Bets

}
