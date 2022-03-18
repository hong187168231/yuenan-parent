package com.indo.game.pojo.dto.rich;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * rich88 活动派奖对象
 */
@Data
public class Rich88ActivityReq {
    // 活动ID
    @JSONField(name = "event_id")
    private String event_id;
    // 奖项ID
    @JSONField(name = "award_id")
    private String award_id;
    // 活动类型 •	紅包：packet 	龍榜：dragon 虎榜：tiger
    @JSONField(name = "activity_type")
    private String activity_type;
    // 玩家账号
    @JSONField(name = "account")
    private String account;
    // 请求行为 派獎：prize
    @JSONField(name = "action")
    private String action;
    // 交易币种
    @JSONField(name = "currency")
    private String currency;
    // 金额 需要根据币种进行转换
    @JSONField(name = "money")
    private BigDecimal money;
}
