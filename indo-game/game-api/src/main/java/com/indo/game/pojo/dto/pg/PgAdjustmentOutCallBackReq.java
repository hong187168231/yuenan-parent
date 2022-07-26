package com.indo.game.pojo.dto.pg;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PgAdjustmentOutCallBackReq {


    /**
     * 运营商独有的身份识别
     */
    @JSONField(name = "operator_token")
    private String operator_token;

    /**
     * PGSoft 与运营商之间共享密码
     */
    @JSONField(name = "secret_key")
    private String secret_key;

    /**
     * 账户
     */
    @JSONField(name = "player_name")
    private String player_name;

    /**
     * 玩家选择的币种
     */
    @JSONField(name = "currency_code")
    private String currency_code;

    /**
     * 金额调整
     * 负数：扣除余额
     * 正数：增加余额
     */
    @JSONField(name = "transfer_amount")
    private BigDecimal transfer_amount;

    /**
     * 调整的参考 ID
     */
    @JSONField(name = "adjustment_id")
    private String adjustment_id;

    /**
     * 交易的唯一标识符
     * 注：
     * • 运营商应使用此参数来检查请求
     * 是否重复并实现幂等操作
     */
    @JSONField(name = "adjustment_transaction_id")
    private String adjustment_transaction_id;

    /**
     * 调整时间
     * (Unix 时间戳，以毫秒为单位)
     */
    @JSONField(name = "adjustment_time")
    private Long adjustment_time;

    /**
     * 调整来源：
     * 900: 外部调整
     * 901: 锦标赛调整
     */
    @JSONField(name = "transaction_type")
    private String transaction_type;

    /**
     * 投注记录的投注类型：
     * 1：真实游戏
     */
    @JSONField(name = "bet_type")
    private Integer bet_type;




}
