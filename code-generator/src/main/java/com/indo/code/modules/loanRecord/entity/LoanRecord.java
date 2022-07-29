package com.indo.code.modules.loanRecord.entity;

import java.math.BigDecimal;
import com.indo.common.pojo.entity.BaseEntity;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    private Integer id;

    @ApiModelProperty(value = "会员ID")
    private Long memId;

    @ApiModelProperty(value = "借款金额")
    private BigDecimal loanAmount;

    @ApiModelProperty(value = "还款金额")
    private BigDecimal backMoney;

    @ApiModelProperty(value = "状态：1未还，2已还，3还部分")
    private Integer states;

    @ApiModelProperty(value = "到期还款截止日期")
    private LocalDateTime backTime;


}
