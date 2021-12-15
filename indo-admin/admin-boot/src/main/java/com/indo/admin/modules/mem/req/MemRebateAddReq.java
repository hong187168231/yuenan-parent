package com.indo.admin.modules.mem.req;

import com.indo.admin.modules.mem.vo.MemBetVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel
public class MemRebateAddReq {


    @ApiModelProperty(value = "返点集合")
    private List<MemBetVo> betList;

}
