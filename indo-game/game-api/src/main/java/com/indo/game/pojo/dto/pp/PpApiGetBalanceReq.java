package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * PP电子查询余额请求对象
 */
@Data
public class PpApiGetBalanceReq {

    /**
     * hash
     */
    @JSONField(name = "hash")
    private String hash;

    /**
     * 用户ID
     */
    @JSONField(name = "externalPlayerId")
    private String externalPlayerId;

}
