package com.indo.user.pojo.vo;

import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.pojo.vo.mem.MemTradingVo;
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
public class MemLevelVo {


    private MemTradingVo tradingVo;

    private List<MemLevel> levelList;


}
