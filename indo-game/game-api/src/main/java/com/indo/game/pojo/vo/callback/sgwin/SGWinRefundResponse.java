package com.indo.game.pojo.vo.callback.sgwin;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SGWinRefundResponse {

    private List Response;

    @JsonProperty("Response")
    public List getResponse() {
        return Response;
    }

    public void setResponse(List response) {
        Response = response;
    }
}
