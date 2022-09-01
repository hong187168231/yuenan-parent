package com.indo.game.pojo.vo.callback.sgwin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SGWinNotifySettleCallBackReq {

    @JSONField(name = "BrandCode")
    private String BrandCode;//	String	vntest	ID of sub company
    @JSONField(name = "Timestamp")
    private Long Timestamp;//	Long	1565837724000	Timestamp of placebet called.
    @JSONField(name = "Sign")
    private String Sign;//	String	M25XWSwHAkKfO9NTRiJ8RD8iKq00ZTpVp3+2gVTRQfRjFy6q6Hlku2qZnLwC7Ges0PCQg82s1d3I1CwKYbBqRg==	Sign will be generated at launch game URL and validate the debit call. Refer to appendix.
    @JSONField(name = "TransactionId")
    private String TransactionId;//	String  	TLD190711529665573	Transaction Id of the debit. Unique ID for each debit call.
    @JSONField(name = "IsResettle")
    private BigDecimal IsResettle;

    @JSONField(name = "Lottery")
    private String Lottery;//	Lottery code.
    @JSONField(name = "DrawNumber")
    private String DrawNumber;//	Number of each draw.
}
