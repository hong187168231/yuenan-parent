package com.indo.game.pojo.entity.awc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GiveTxns {
    @ApiModelProperty(value = "辨认交易时间依据")
    private String txTime;

    @ApiModelProperty(value = "玩家获得的活动派彩")
    private String amount;

    @ApiModelProperty(value = "玩家货币代码")
    private String currency;

    @ApiModelProperty(value = "活动的交易代码")
    private String promotionTxId;

    @ApiModelProperty(value = "活动代码")
    private String promotionId;

    @ApiModelProperty(value = "活动种类的代码")
    private String promotionTypeId;

    @ApiModelProperty(value = "玩家 ID")
    private String userId;

    @ApiModelProperty(value = "游戏平台名称")
    private String platform;

}
