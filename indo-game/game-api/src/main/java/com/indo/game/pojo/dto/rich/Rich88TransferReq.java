package com.indo.game.pojo.dto.rich;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * rich88 交易请求对象
 */
@Data
public class Rich88TransferReq {
    // 玩家账号
    @JSONField(name = "account")
    private String account;
    // 交易序号 同transfer_no 二选一
    @JSONField(name = "record_id")
    private String record_id;
    // 交易序号  同record_id 二选一
    @JSONField(name = "transfer_no")
    private String transfer_no;
    // 交易类型 提款： withdraw 取消提款： rollback 存款： deposit
    @JSONField(name = "action")
    private String action;
    // 游戏CODE
    @JSONField(name = "game_code")
    private String game_code;
    // 游戏回合ID
    @JSONField(name = "round_id")
    private String round_id;
    // 交易币种
    @JSONField(name = "currency")
    private String currency;
    // 金额 需要根据币种进行转换
    @JSONField(name = "money")
    private BigDecimal money;
}
