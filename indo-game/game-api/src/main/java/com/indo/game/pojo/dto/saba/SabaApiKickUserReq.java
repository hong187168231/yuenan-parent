package com.indo.game.pojo.dto.saba;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class SabaApiKickUserReq {
    @JSONField(name = "vendor_member_id")
    private String vendor_member_id;
    @JSONField(name = "vendor_id")
    private String vendor_id;
}
