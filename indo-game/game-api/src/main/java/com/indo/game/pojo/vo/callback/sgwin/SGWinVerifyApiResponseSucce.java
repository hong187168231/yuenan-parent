package com.indo.game.pojo.vo.callback.sgwin;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class SGWinVerifyApiResponseSucce {
    private Boolean Success;
    @JsonProperty("Success")
    public Boolean getSuccess() {
        return Success;
    }
    public void setSuccess(Boolean success) {
        Success = success;
    }
}
