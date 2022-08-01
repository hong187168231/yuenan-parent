package com.indo.user.pojo.req.gift;

import com.indo.common.enums.GiftNameEnum;
import com.indo.common.enums.GiftTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName : RegisterReq
 * @Description : 注册请求参数类
 * @Author :puff
 * @Date: 2021-08-25 16:16
 */
@Data
@ApiModel(value = "礼金领取请求参数类")
public class GiftReceiveReq {

    @ApiModelProperty(value = "礼金类型", required = true)
    private GiftTypeEnum giftTypeEnum;

    @ApiModelProperty(value = "礼金名称", required = true)
    private GiftNameEnum giftNameEnum;

    @ApiModelProperty(value = "礼金金额", required = false)
    private BigDecimal giftAmount;

    @ApiModelProperty(value = "领取等级", required = true)
    private Integer level;

}
