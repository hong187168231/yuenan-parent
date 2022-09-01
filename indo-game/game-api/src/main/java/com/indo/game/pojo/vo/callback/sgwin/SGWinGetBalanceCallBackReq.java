package com.indo.game.pojo.vo.callback.sgwin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class SGWinGetBalanceCallBackReq {

    @JSONField(name = "BrandCode")
    private String BrandCode;//	String	vntest	ID of sub company
    @JSONField(name = "Timestamp")
    private Long Timestamp;//	Long	1565837724000	Timestamp of the getbalance called.
    @JSONField(name = "Sign")
    private String Sign;//	String	M25XWSwHAkKfO9NTRiJ8RD8iKq00ZTpVp3+2gVTRQfRjFy6q6Hlku2qZnLwC7Ges0PCQg82s1d3I1CwKYbBqRg==	Sign will be generated at launch game URL and validate the debit call. Refer to appendix.
    @JSONField(name = "MemberId")
    private String MemberId;//	String 	member1	ID of the member
    @JSONField(name = "Currency")
    private String Currency;//	String 	VND	Currency code

}
