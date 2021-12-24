package com.indo.admin.modules.mem.vo;

import com.indo.admin.modules.mem.req.MemRebateAddReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MemRebateVo {


    @ApiModelProperty(value = "返点集合")
    private List<MemBetVo> betList;



}
