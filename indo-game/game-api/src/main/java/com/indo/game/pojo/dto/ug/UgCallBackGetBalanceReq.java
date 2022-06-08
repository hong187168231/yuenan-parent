package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UgCallBackGetBalanceReq extends UgCallBackParentReq {

    private String userId;

}
