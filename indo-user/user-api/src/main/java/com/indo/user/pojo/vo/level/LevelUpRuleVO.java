package com.indo.user.pojo.vo.level;

import com.indo.user.pojo.bo.MemTradingBO;
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
public class LevelUpRuleVO {

    @ApiModelProperty(value = "所需存款")
    private BigDecimal needDeposit;

    @ApiModelProperty(value = "所需投注")
    private BigDecimal needBet;

}
