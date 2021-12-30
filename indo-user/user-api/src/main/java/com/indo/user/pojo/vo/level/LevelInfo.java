package com.indo.user.pojo.vo.level;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author puff
 * @since 2021-08-31
 */
@Data
@ApiModel
public class LevelInfo {

    @ApiModelProperty(value = "会员等级")
    private Integer level;

    @ApiModelProperty(value = "所需存款")
    private Integer needDeposit;

    @ApiModelProperty(value = "所需投注")
    private Integer needBet;

    @ApiModelProperty(value = "促销奖金")
    private Integer promotionGift;

    @ApiModelProperty(value = "专享福利")
    List<Gift> giftList;


}
