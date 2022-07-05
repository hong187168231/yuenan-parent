package com.indo.game.pojo.dto.saba;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SabaApiGetUrlReq{
    @JSONField(name = "platform")
    private String platform;
    @JSONField(name = "vendor_member_id")
    private String vendor_member_id;
    @JSONField(name = "vendor_id")
    private String vendor_id;
}
