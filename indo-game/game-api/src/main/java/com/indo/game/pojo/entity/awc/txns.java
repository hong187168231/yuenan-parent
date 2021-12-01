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
public class txns {
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "游戏商注单号")
    private String platformTxId;

    @ApiModelProperty(value = "玩家 ID")
    private String userId;

    @ApiModelProperty(value = "玩家货币代码")
    private String currency;

    @ApiModelProperty(value = "游戏平台名称")
    private String platform;

    @ApiModelProperty(value = "平台游戏类型")
    private String gameType;

    @ApiModelProperty(value = "平台游戏代码")
    private String gameCode;

    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    @ApiModelProperty(value = "游戏平台的下注项目")
    private String betType;

    @ApiModelProperty(value = "下注金额")
    private String betAmount;

    @ApiModelProperty(value = "玩家下注时间")
    private String betTime;

    @ApiModelProperty(value = "游戏商的回合识别码")
    private String roundId;

    @ApiModelProperty(value = "游戏讯息会由游戏商以 JSON 格式提供")
    private String gameInfo;

    @ApiModelProperty(value = "更新时间 (遵循 ISO8601 格式)")
    private String updateTime;

    @ApiModelProperty(value = "真实下注金额")
    private String realBetAmount;

    @ApiModelProperty(value = "真实返还金额")
    private String realWinAmount;

    @ApiModelProperty(value = "返还金额 (包含下注金额)")
    private String winAmount;

    @ApiModelProperty(value = "游戏平台有效投注")
    private String turnover;

    @ApiModelProperty(value = "辨认交易时间依据")
    private String txTime;

    @ApiModelProperty(value = "返还金额当局的游戏商注单号")
    private String refundPlatformTxId;

    @ApiModelProperty(value = "请依据此参数来决定结算方法")
    private String settleType;

//- 2 Void game 游戏无效、现场操作问题等
//- 9 Cheat (hide in the report) 有作弊 (不会呈现在后台报表)
    @ApiModelProperty(value = "标示无效的原因")
    private String voidType;

    @ApiModelProperty(value = "判断玩家当前余额是否足够负担此笔 betNSettle 请求所需的金额")
    private String requireAmount;

    @ApiModelProperty(value = "玩家获得的活动派彩")
    private String amount;

    @ApiModelProperty(value = "活动的交易代码")
    private String promotionTxId;

    @ApiModelProperty(value = "活动代码")
    private String promotionId;

    @ApiModelProperty(value = "活动种类的代码")
    private String promotionTypeId;

    @ApiModelProperty(value = "打赏给直播主的金额")
    private String tip;

    @ApiModelProperty(value = "打赏资讯，此参数仅游戏商有提供资讯时才会出现")
    private String tipinfo;


    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
