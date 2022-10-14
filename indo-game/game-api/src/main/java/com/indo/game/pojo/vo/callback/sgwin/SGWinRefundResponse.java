package com.indo.game.pojo.vo.callback.sgwin;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SGWinRefundResponse {
    @JsonProperty("Response")
    private List Response;
}
