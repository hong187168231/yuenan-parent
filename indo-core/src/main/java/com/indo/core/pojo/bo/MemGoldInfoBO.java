package com.indo.core.pojo.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
@Data
public class MemGoldInfoBO {

    @ApiModelProperty(value = "用户ID")
    private Long memId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "可提金额")
    private BigDecimal canAmount;

}
