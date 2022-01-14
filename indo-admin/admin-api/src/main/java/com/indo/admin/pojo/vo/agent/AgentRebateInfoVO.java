package com.indo.admin.pojo.vo.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author xxx
 * @since 2022-01-10
 */
@Data
@ApiModel(value = "agentRebateRecord对象", description = "")
public class AgentRebateInfoVO implements Serializable {


    @ApiModelProperty(value = "佣金")
    private BigDecimal rebateAmount;


}
