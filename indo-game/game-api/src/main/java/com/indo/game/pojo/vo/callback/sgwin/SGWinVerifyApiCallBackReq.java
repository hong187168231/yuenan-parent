package com.indo.game.pojo.vo.callback.sgwin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class SGWinVerifyApiCallBackReq {

    @JSONField(name = "BrandCode")
    private String BrandCode;//	String	vntest	ID of sub company
}
