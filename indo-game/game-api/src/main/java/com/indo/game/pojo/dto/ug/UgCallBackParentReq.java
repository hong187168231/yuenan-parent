package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UgCallBackParentReq {

    private String APIPassword;

    public String getAPIPassword() {
        return APIPassword;
    }

    public void setAPIPassword(String APIPassword) {
        this.APIPassword = APIPassword;
    }
}
