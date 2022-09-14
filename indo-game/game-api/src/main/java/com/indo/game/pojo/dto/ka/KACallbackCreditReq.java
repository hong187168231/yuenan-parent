package com.indo.game.pojo.dto.ka;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * KA 游戏派彩请求对象
 */
@Data
public class KACallbackCreditReq extends KACallbackCommonReq {
    // 交易序号
    @JSONField(name = "transactionId")
    private String transactionId;

    // 派彩金额 已经乘以100
    @JSONField(name = "amount")
    private BigDecimal amount;

    // 派彩类型 例如： "BonusPick"或“Progressive”
    @JSONField(name = "type")
    private String type;

    // 奖励小游戏点击次数 ， 非必填
    @JSONField(name = "creditIndex")
    private Long creditIndex;

}
