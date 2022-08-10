package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 借款记录表
 * </p>
 *
 * @author xxx
 * @since 2022-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="LoanRecord对象", description="借款记录表")
public class LoanRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "会员ID")
    private Long memId;

    @ApiModelProperty(value = "借款金额")
    private BigDecimal loanAmount = new BigDecimal(0);

    @ApiModelProperty(value = "还款金额")
    private BigDecimal backMoney = new BigDecimal(0);

    @ApiModelProperty(value = "状态：1未还，2已还，3还部分")
    private Integer states;

    @ApiModelProperty(value = "到期还款截止日期")
    private LocalDateTime backTime;


}
