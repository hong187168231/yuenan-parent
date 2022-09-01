package com.indo.game.pojo.dto.cq;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class CqSportsRefudsCallBackReq {

    /**
     * 使用者帳號
     */
    @JSONField(name = "mtcode")
    private String[] mtcode;

}
