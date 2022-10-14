package com.indo.game.pojo.vo.callback.sgwin;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SGWinVerifyApiResponseSucce {
    @JsonProperty("Success")
    private Boolean Success;

}
