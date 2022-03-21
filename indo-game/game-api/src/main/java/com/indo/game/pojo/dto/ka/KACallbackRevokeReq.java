package com.indo.game.pojo.dto.ka;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

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

}
