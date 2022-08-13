package com.indo.admin.pojo.vo.mem;

import com.indo.core.pojo.vo.mem.MemBetVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MemRebateVo {


    @ApiModelProperty(value = "返点集合")
    private List<MemBetVo> betList;



}
