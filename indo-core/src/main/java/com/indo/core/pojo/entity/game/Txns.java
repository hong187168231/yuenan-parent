package com.indo.core.pojo.entity.game;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("game_txns")
public class Txns {
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "游戏商注单号")
    private String platformTxId;

    @ApiModelProperty(value = "此交易是否是投注")
    private Boolean bet;

    @ApiModelProperty(value = "玩家 账号")
    private String userId;

    @ApiModelProperty(value = "玩家货币代码")
    private String currency;

    @ApiModelProperty(value = "游戏平台代码")
    private String platform;

    @ApiModelProperty(value = "游戏平台英文名称")
    private String platformEnName;

    @ApiModelProperty(value = "游戏平台中文名称")
    private String platformCnName;

    @ApiModelProperty(value = "平台游戏类型")
    private String gameType;

    @ApiModelProperty(value = "平台游戏代码")
    private String gameCode;

    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    @ApiModelProperty(value = "游戏平台的下注项目")
    private String betType;

    @ApiModelProperty(value = "下注金额")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "中奖金额（赢为正数，亏为负数）")
    private BigDecimal winningAmount;

    @ApiModelProperty(value = "玩家下注时间")
    private String betTime;

    @ApiModelProperty(value = "游戏商的回合识别码")
    private String roundId;

    @ApiModelProperty(value = "游戏讯息会由游戏商以 JSON 格式提供")
    private String gameInfo;

    @ApiModelProperty(value = "更新时间 (遵循 ISO8601 格式)")
    private String updateTime;

    @ApiModelProperty(value = "真实下注金额,需增加在玩家的金额")
    private BigDecimal realBetAmount;

    @ApiModelProperty(value = "真实返还金额或游戏赢分")
    private BigDecimal realWinAmount;

    @ApiModelProperty(value = "返还金额 (包含下注金额)")
    private BigDecimal winAmount;

    @ApiModelProperty(value = "赌注的结果 : AWC平台{赢:0,输:1,平手:2}  UG平台{0:Draw,1:Win,2:Lose,3:Win Half,4:Lose Half}")
    private int resultType;

    @ApiModelProperty(value = "有效投注金额")
    private BigDecimal turnover;

    @ApiModelProperty(value = "辨认交易时间依据")
    private String txTime;

    @ApiModelProperty(value = "返回单号")
    private String rePlatformTxId;

    @ApiModelProperty(value = "返还金额当局的游戏商注单号")
    private String refundPlatformTxId;

    @ApiModelProperty(value = "请依据此参数来决定结算方法")
    private String settleType;

    //- 2 Void game 游戏无效、现场操作问题等
//- 9 Cheat (hide in the report) 有作弊 (不会呈现在后台报表)
    @ApiModelProperty(value = "标示无效的原因")
    private String voidType;

    @ApiModelProperty(value = "判断玩家当前余额是否足够负担此笔 betNSettle 请求所需的金额。jdb彩金贡献值")
    private BigDecimal requireAmount;

    @ApiModelProperty(value = "玩家获得的活动派彩。jdb赢得彩金金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "活动的交易代码")
    private String promotionTxId;

    @ApiModelProperty(value = "活动代码")
    private String promotionId;

    @ApiModelProperty(value = "活动种类的代码")
    private String promotionTypeId;

    @ApiModelProperty(value = "打赏给直播主的金额 *jdb玩家合理最小余额")
    private BigDecimal tip;

    @ApiModelProperty(value = "赔率")
    private BigDecimal odds;

    @ApiModelProperty(value = "赔率类型")
    private String oddsType;

    @ApiModelProperty(value = "打赏资讯，此参数仅游戏商有提供资讯时才会出现")
    private String tipinfo;

    @ApiModelProperty(value = "操作状态")
    private String status;

    @ApiModelProperty(value = "操作名称")
    @NotNull(message = "操作名称")
    private String method;

    @ApiModelProperty(value = "游戏回调方法名称")
    @NotNull(message = "游戏回调方法名称")
    private String gameMethod;

    @ApiModelProperty(value = "余额")
    @NotNull(message = "余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "游戏分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "游戏分类名称")
    private String categoryName;

    @ApiModelProperty(value = "投注 IP")
    private String betIp;//  string 是 投注 IP

    @ApiModelProperty(value = "代理编号")
    private String agentId;//  long 是 代理编号

    @ApiModelProperty(value = "组别佣金代码")
    private String groupComm;//  string 是 组别佣金代码

    @ApiModelProperty(value = "混合过关类型 ID")
    private int mpId;//  int 是 混合过关类型 ID

    @ApiModelProperty(value = "投注方式{1:PC,2:Wap,4:Smart}")
    private int betWay;//  int 是 投注方式

    @ApiModelProperty(value = "注单排序值")
    private String sortNo;

    @ApiModelProperty(value = "报表日期")
    private String reportDate;

    @ApiModelProperty(value = "奖金游戏")
    private Integer hasBonusGame;// Integer 奖金游戏0: 否   1: 是
    @ApiModelProperty(value = "博取游戏")
    private Integer hasGamble;// Integer 博取游戏0: 否  1: 是
    @ApiModelProperty(value = "游戏区域")
    private Integer roomType;
    // Integer 游戏区域
    //-1:大厅（成就游戏）
    // 0:小压码区
    //1:中压码区
    //2:大压码区
    //※各压码区称号会依据机台类型有所不同
}
