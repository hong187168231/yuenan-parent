package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PP电子令牌权限校验请求对象
 */
@Data
public class PpAuthenticateCallBackReq {

    /**
     * hash
     */
    @JSONField(name = "hash")
    private String hash;

    /**
     * 游戏供应商ID
     */
    @JSONField(name = "providerId")
    private String providerId;

    /**
     * 游戏的标识符。
     */
    @JSONField(name = "gameId")
    private String gameId;

    /**
     * 玩家令牌
     */
    @JSONField(name = "token")
    private String token;

    /**
     * IP地址。
     */
    @JSONField(name = "ipAddress")
    private String ipAddress;

}
