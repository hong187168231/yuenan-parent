package com.indo.game.pojo.dto.saba;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SabaApiCreateMemberReq extends SabaApiRequestParentData{
    @JSONField(name = "vendor_Member_ID")
    private String vendor_Member_ID;
    @JSONField(name = "username")
    private String username;
    @JSONField(name = "oddsType")
    private String oddsType;
    @JSONField(name = "currency")
    private String currency;
    @JSONField(name = "maxTransfer")
    private BigDecimal maxTransfer;
    @JSONField(name = "minTransfer")
    private BigDecimal minTransfer;
}
