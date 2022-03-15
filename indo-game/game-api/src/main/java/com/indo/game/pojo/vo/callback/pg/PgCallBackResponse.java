package com.indo.game.pojo.vo.callback.pg;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class PgCallBackResponse {
    private JSONObject data;
    private JSONObject error;
}
