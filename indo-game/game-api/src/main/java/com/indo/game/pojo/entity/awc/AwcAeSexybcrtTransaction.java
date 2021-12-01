package com.indo.game.pojo.entity.awc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@TableName("game_awc_ae_sexybcrt_Transaction")
public class AwcAeSexybcrtTransaction {
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "平台游戏类型")
    private String gameType;
//    string <= 10 characters
//    platform game type 平台游戏类型
//    Example 范例：LIVE
//
//    Please refer to 8.8. List of Platforms for details.
//            详情请参考 8.8. 游戏平台对照表

    @ApiModelProperty(value = "返还金额 (包含下注金额)")
    private String winAmount;
//    number <float> decimal places <= 4
//    Return amount (bet amount included)
//    返还金额 (包含下注金额)

    @ApiModelProperty(value = "用于区分注单结果是否有更改")
    private String settleStatus;
//    number <= 1 characters
//    Display the settle status of the transaction to distinguish if the transaction involves result change.
//
//    Normal settle process:
//    Default: 0
//    With result change:
//    Unsettle/ Voidsettle/ Unvoidsettle: 1
//    Voidbet: -1
//    用于区分注单结果是否有更改
//
//    正常状况：
//    预设：0
//    结果更改过状况：
//    Unsettle / Voidsettle / Unvoidsettle: 1
//    Voidbet: -1
//    Please refer to 8.7. Relation of settleStatus and txStatus for details.
//            详情请参考 8.7. settleStatus 和 txStatus 的关系

    @ApiModelProperty(value = "真实下注金额")
    private String realBetAmount;
//    number <float> decimal places <= 4
//    Real bet amount
//            真实下注金额
//
//    For more details, please refer to 8.2. Transaction and Terms
//    若想了解更多，请参考 8.2. 交易词汇和定义

    @ApiModelProperty(value = "真实返还金额")
    private String realWinAmount;
//    number <float> decimal places <= 4
//    Real win amount
//            真实返还金额
//
//    For more details, please refer to 8.2. Transaction and Terms
//    若想了解更多，请参考 8.2. 交易词汇和定义

    @ApiModelProperty(value = "辨认交易时间依据")
    private String txTime;
//    string <= 25 characters
//    biz time(ISO8601 format)
//    Example 范例：2020-02-03T12:02:32+08:00
//
//    The time of transactions recognize.
//            辨认交易时间依据

    @ApiModelProperty(value = "更新时间 (遵循 ISO8601 格式)")
    private String updateTime;
//    string <= 25 characters
//    Update time (ISO8601 format)
//
//    Please use the updateTime of last record as the timeFrom parameter of your next round fetching.
//    Note: If there is no data or no update data for a certain value, set the next value timeFrom to one minute before the current time.
//    更新时间 (遵循 ISO8601 格式)
//
//    请使用拉取最后一张注单的更新时间当做取下一次拉帐的 timeFrom 参数
//    注意：若某次取值无资料 或 无更新资料，则将下次取值 timeFrom 设为现在时间的前一分钟

    @ApiModelProperty(value = "玩家 ID")
    private String userId;
//    string <= 16 characters
//    User ID 玩家 ID

    @ApiModelProperty(value = "游戏平台的下注项目")
    private String betType;
//    string <= 50 characters
//    Platform bet type 游戏平台的下注项目
//    Example 范例：
//    Banker Player Tie
//
//    The parameter only supports in the following platforms.
//    Supported platform:
//    PG, SEXYBCRT, AWS, VENUS, SV388, AE, Lotto, SABA, HORSEBOOK, E1SPORT, AELIVECOMM
//
//            此参数仅支持以下平台
//    支持平台：
//    PG, SEXYBCRT, AWS, VENUS, SV388, AE, Lotto, SABA, HORSEBOOK, E1SPORT, AELIVECOMM

    @ApiModelProperty(value = "游戏平台名称")
    private String platform;
//    string <= 20 characters
//    Platform name 游戏平台名称
//    Example 范例：SEXYBCRT
//
//    Please refer to 8.8. List of Platforms for details.
//            详情请参考 8.8. 游戏平台对照表

    @ApiModelProperty(value = "若无带入参数则默认回传数值")
    private String txStatus;
//    number
//    Transaction status, ref transaction status 交易状态
//
//    If not pass the param, default txStatus include below:
//    若无带入参数则默认回传数值包含以下：
//            -1 Cancel bet 取消投注
//1 Settled 已结账
//2 Void 注单无效
//9 Invalid 无效交易
//
//    Please refer to 8.6.Transaction status for details.
//            详情请参考 8.6.交易类型

    @ApiModelProperty(value = "下注金额")
    private String betAmount;
//    number <float> decimal places <= 4
//    Bet amount (how much did user bet)
//    下注金额

    @ApiModelProperty(value = "游戏名称")
    private String gameName;
//    string <= 50 characters
//    The game name
//            游戏名称
//

    @ApiModelProperty(value = "游戏商注单号")
    private String platformTxId;
//    string <= 50 characters
//    Platform transaction ID 游戏商注单号
//    Example 范例：BAC-00101 TXN11053 LH-3105
//
//            *platform + platformTxId will be unique
//*如果您需要唯一值，请使用 platform + platformTxId

    @ApiModelProperty(value = "玩家下注时间")
    private String betTime;
//    string <= 25 characters
//    Transaction time, the time player bets (ISO8601 format)
//    玩家下注时间
//

    @ApiModelProperty(value = "平台游戏代码")
    private String gameCode;
//    string <= 50 characters
//    Platform game code
//            平台游戏代码
//    Example 范例：MX-LIVE-001

    @ApiModelProperty(value = "玩家货币代码")
    private String currency;
//    string <= 4 characters
//    Player currency code 玩家货币代码
//
//    Please refer to 8.3. List of Currency Types for details.
//            详细请参考 8.3. 货币类型列表

    @ApiModelProperty(value = "累积奖金的下注金额")
    private String jackpotBetAmount;
//    number <float> decimal places <= 4
//    Jackpot bet amount
//            累积奖金的下注金额
//※AWC do not support jackpot yet
//※AWC 目前尚未支持 jackpot

    @ApiModelProperty(value = "累积奖金的获胜金额")
    private String jackpotWinAmount;
//    number <float> decimal places <= 4
//    Jackpot win amount
//            累积奖金的获胜金额
//※AWC do not support jackpot yet
//※AWC 目前尚未支持 jackpot

    @ApiModelProperty(value = "游戏平台有效投注")
    private String turnover;
//    number <float> decimal places <= 4
//    Platform valid bet
//            游戏平台有效投注

    @ApiModelProperty(value = "游戏商的回合识别码")
    private String roundId;
//    string <= 20 characters
//    Round ID 游戏商的回合识别码
//    Example 范例：Mexico-01-GA17590001

    @ApiModelProperty(value = "游戏讯息会由游戏商以 JSON 格式提供")
    private String gameInfo;
//    string
//    Display game info from game providers in JSON format
//    游戏讯息会由游戏商以 JSON 格式提供

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
