package com.indo.user.pojo.vo.level;

import com.indo.user.pojo.bo.MemTradingBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class MemLevelVo {

    @ApiModelProperty(value = "会员交易信息")
    private MemTradingBO tradingVo;

    @ApiModelProperty(value = "等级信息列表")
    private List<LevelInfo> levelList;


}
