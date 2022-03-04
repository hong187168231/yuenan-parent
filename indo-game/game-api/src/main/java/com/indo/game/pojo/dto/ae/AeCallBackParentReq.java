package com.indo.game.pojo.dto.ae;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class AeCallBackParentReq {
    /**
     * 运营商代码
     */
    @JSONField(name = "merchantId")
    private String merchantId;
    /**
     * 币别
     */
    @JSONField(name = "currency")
    private String currency;
    /**
     * 玩家账号
     */
    @JSONField(name = "accountId")
    private String accountId;
    /**
     * 毫秒
     */
    @JSONField(name = "currentTime")
    private String currentTime;
    /**
     * 加币
     */
    @JSONField(name = "sign")
    private String sign;


}
