package com.indo.game.pojo.dto.ka;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * KA 取消交易回调请求对象
 */
@Data
public class KACallbackRevokeReq extends KACallbackCommonReq {
    // 交易序号
    @JSONField(name = "transactionId")
    private String transactionId;

    // 当前游戏回合
    @JSONField(name = "round")
    private Long round;

    @JSONField(name = "win")
    private BigDecimal win;

    @JSONField(name = "bet")
    private BigDecimal bet;

    @JSONField(name = "revokedAction")
    private String revokedAction;

    @JSONField(name = "playerId")
    private String playerId;


    List<KACallbackPlayReq> list = new ArrayList<>();

}
