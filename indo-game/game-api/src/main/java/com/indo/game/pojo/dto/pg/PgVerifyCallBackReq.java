package com.indo.game.pojo.dto.pg;

import com.alibaba.fastjson.annotation.JSONField;


import java.math.BigDecimal;

import lombok.Data;

@Data
public class PgVerifyCallBackReq {


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
     * 运营商系统生成的令牌
     */
    @JSONField(name = "operator_player_session")
    private String operator_player_session;

    /**
     * operator_param 值
     */
    @JSONField(name = "custom_parameter")
    private String custom_parameter;


    /**
     * 游戏的独有代码
     */
    @JSONField(name = "game_id")
    private Integer game_id;
    /**
     * 账户
     */
    @JSONField(name = "player_name")
    private String player_name;
    /**
     * 母注单的唯一标识符
     */
    @JSONField(name = "parent_bet_id")
    private String parent_bet_id;
    /**
     * 子投注的唯一标识符
     */
    @JSONField(name = "bet_id")
    private String bet_id;
    /**
     * 玩家选择的币种
     */
    @JSONField(name = "currency_code")
    private String currency_code;
    /**
     * 玩家平台
     */
    @JSONField(name = "platform")
    private String platform;
    /**
     * 赢得金额
     */
    @JSONField(name = "bet_amount")
    private BigDecimal bet_amount;
    /**
     * 赢得金额
     */
    @JSONField(name = "win_amount")
    private BigDecimal win_amount;
    /**
     * 玩家的输赢金额
     */
    @JSONField(name = "transfer_amount")
    private BigDecimal transfer_amount;
    /**
     * 交易的唯一标识符
     */
    @JSONField(name = "transaction_id")
    private BigDecimal transaction_id;
    /**
     * 投注交易的唯一标识符
     */
    @JSONField(name = "bet_transaction_id")
    private BigDecimal bet_transaction_id;
    /**
     * 钱包类型
     */
    @JSONField(name = "wallet_type")
    private BigDecimal wallet_type;
    /**
     * 投注的开始时间
     */
    @JSONField(name = "create_time")
    private Long create_time;
    /**
     * 投注的更新时间
     */
    @JSONField(name = "updated_time")
    private Long updated_time;
    /**
     * 表示该请求是否是为进行验证而重新 发送的交易 True: 重新发送的交易 False: 正常交易
     */
    @JSONField(name = "is_validate_bet")
    private Boolean is_validate_bet;

    /**
     * 表示该请求是否是待处理投注的调整 或正常交易 True: 调整 False: 正常交易
     */
    @JSONField(name = "is_validate_bet")
    private Boolean is_adjustment;

    /**
     * 表示该请求在第一轮投注中的投注金 额是否为 0 True: 在第一轮投注中的投注金额为 0（针对至尊百家乐的飞牌操作） False: 在第一轮投注中的投注金额大 于 0
     */
    @JSONField(name = "is_parent_zero_stake")
    private Boolean is_parent_zero_stake;

    /**
     * 表示该交易是否为投注 True: bet_amount > 0 False: bet_amount = 0
     */
    @JSONField(name = "is_wager")
    private Boolean is_wager;

    /**
     * 调整的参考 ID
     */
    @JSONField(name = "adjustment_id")
    private String adjustment_id;
    /**
     * 交易的唯一标识符
     */
    @JSONField(name = "adjustment_transaction_id")
    private String adjustment_transaction_id;
}
