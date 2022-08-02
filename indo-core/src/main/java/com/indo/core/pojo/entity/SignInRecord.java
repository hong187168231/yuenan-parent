package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 签到记录表
 * </p>
 *
 * @author xxx
 * @since 2022-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SignInRecord对象", description="签到记录表")
public class SignInRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "会员ID")
    private Long memId;

    @ApiModelProperty(value = "签到金额")
    private BigDecimal signMoney;


}
