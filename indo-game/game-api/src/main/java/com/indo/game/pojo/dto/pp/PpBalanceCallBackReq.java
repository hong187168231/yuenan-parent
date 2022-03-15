package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * PP电子查询余额请求对象
 */
@Data
public class PpBalanceCallBackReq {

    /**
     * hash
     */
    @JSONField(name = "hash")
    private String hash;

    /**
     * 玩家令牌
     */
    @JSONField(name = "token")
    private String token;

    /**
     * 游戏供应商ID
     */
    @JSONField(name = "providerId")
    private String providerId;

    /**
     * 娱乐场运营商系统中的用户标识符。
     */
    @JSONField(name = "userId")
    private String userId;

}
