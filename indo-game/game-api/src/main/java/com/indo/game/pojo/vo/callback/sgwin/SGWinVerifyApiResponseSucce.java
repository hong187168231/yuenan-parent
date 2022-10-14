package com.indo.game.pojo.vo.callback.sgwin;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SGWinVerifyApiResponseSucce {
    @JSONField(name = "Success")
    private Boolean Success;

    @JSONField(name = "Success")
    public Boolean getSuccess() {
        return Success;
    }

    @JSONField(name = "Success")
    public void setSuccess(Boolean success) {
        Success = success;
    }
}
