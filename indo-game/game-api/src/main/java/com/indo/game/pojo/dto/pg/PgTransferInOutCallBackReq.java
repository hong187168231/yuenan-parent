package com.indo.game.pojo.dto.pg;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PgTransferInOutCallBackReq {

    /**
     * 请求的唯一标识符（GUID）
     */
    @JSONField(name = "trace_id")
    private String trace_id;

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
     * 账户
     */
    @JSONField(name = "player_name")
    private String player_name;

    /**
     * 游戏的独有代码
     */
    @JSONField(name = "game_id")
    private Integer game_id;

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
    private String transaction_id;

    /**
     * 钱包类型
     */
    @JSONField(name = "wallet_type")
    private String wallet_type;

    /**
     * True：普通旋转（扣除免费游戏次数）
     * False：免费旋转
     */
    @JSONField(name = "is_minus_count")
    private Boolean is_minus_count;

    /**
     * 投注记录的投注类型：
     * 1：真实游戏
     */
    @JSONField(name = "bet_type")
    private Integer bet_type;

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
    @JSONField(name = "is_adjustment")
    private Boolean is_adjustment;

    /**
     * 表示该请求在第一轮投注中的投注金 额是否为 0 True: 在第一轮投注中的投注金额为 0（针对至尊百家乐的飞牌操作） False: 在第一轮投注中的投注金额大 于 0
     */
    @JSONField(name = "is_parent_zero_stake")
    private Boolean is_parent_zero_stake;

    /**
     * 表示旋转类型
     * True: 特色旋转
     * False: 普通旋转
     */
    @JSONField(name = "is_feature")
    private Boolean is_feature;

    /**
     * 表示购买奖金游戏
     * 注：
     *  仅适用于具有购买奖金游戏功能的游戏
     */
    @JSONField(name = "is_feature_buy")
    private Boolean is_feature_buy;

    /**
     * 表示该交易是否为投注 True: bet_amount > 0 False: bet_amount = 0
     */
    @JSONField(name = "is_wager")
    private Boolean is_wager;

    /**
     * 表示当前游戏投注是否已结束
     */
    @JSONField(name = "is_end_round")
    private Boolean is_end_round;

    /**
     * 免费游戏的唯一标识符
     * 只有在免费游戏分配给玩家时才会出现。
     * 注：
     * ● 仅适用于以外部应用编程接口
     * API 创建的免费游戏
     */
    @JSONField(name = "ree_game_transaction_id")
    private String ree_game_transaction_id;

    /**
     * 免费游戏名称
     * 只有在免费游戏分配给玩家时才会出现。
     */
    @JSONField(name = "free_game_name")
    private String free_game_name;

    /**
     * 免费游戏的唯一标识符
     * 只有在免费游戏分配给玩家时才会出现。
     */
    @JSONField(name = "free_game_id")
    private Integer free_game_id;

    /**
     * 红利游戏的唯一标识符
     * 只有在红利分配给玩家时才会出现。
     * 注：
     * ● 仅适用于以外部应用编程接口
     * API 创建的红利游戏
     */
    @JSONField(name = "bonus_transaction_id")
    private String bonus_transaction_id;
    /**
     * 红利游戏名称
     * 只有在红利分配给玩家时才会出现。
     */
    @JSONField(name = "bonus_name")
    private String bonus_name;
    /**
     * 红利游戏的唯一标识符
     * 只有在红利分配给玩家时才会出现
     */
    @JSONField(name = "bonus_id")
    private Integer bonus_id;

    /**
     * 红利钱包的红利总金额
     * 只有在玩家选择用现金完成红利时才
     * 会出现。
     */
    @JSONField(name = "bonus_balance_amount")
    private BigDecimal bonus_balance_amount;

    /**
     * 红利游戏中玩家需要达到的流水金额
     * 只有在玩家选择用现金完成红利时才
     * 会出现。
     */
    @JSONField(name = "bonus_ratio_amount")
    private BigDecimal bonus_ratio_amount;

    /**
     * 玩家的累积奖池贡献金额
     * 注：
     * ● 仅适用于累积奖池游戏
     */
    @JSONField(name = "jackpot_rtp_contribution_amount")
    private BigDecimal jackpot_rtp_contribution_amount;
}
