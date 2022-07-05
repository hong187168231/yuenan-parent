package com.indo.game.pojo.dto.saba;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class SabaApiRequestParentData {
    @JSONField(name = "operatorId")
    private String operatorId;
    @JSONField(name = "vendor_id")
    private String vendor_id;
}
