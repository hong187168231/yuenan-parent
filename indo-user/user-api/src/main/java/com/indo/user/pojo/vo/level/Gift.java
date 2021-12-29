package com.indo.user.pojo.vo.level;

import com.indo.common.enums.GiftEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

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
public class Gift {


    @ApiModelProperty(value = "领取状态 0 不可领取 1 可领取 2 已经领取 ")
    private Integer receiveStatus = 0;

    @ApiModelProperty(value = "礼金金额")
    private Integer amount;

    @ApiModelProperty(value = "礼金名称")
    private String giftName;

    @ApiModelProperty(value = "礼金类型")
    private GiftEnum giftEnum;


}
