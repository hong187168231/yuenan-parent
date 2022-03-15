package com.indo.game.pojo.dto.ae;


import lombok.Data;

@Data
public class AeCallBackParentReq {
    /**
     * 运营商代码
     */
    private String merchantId;
    /**
     * 币别
     */
    private String currency;
    /**
     * 玩家账号
     */
    private String accountId;
    /**
     * 毫秒
     */
    private String currentTime;
    /**
     * 加币
     */
    private String sign;


}
