package com.indo.admin.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MemGrantRebateReq {

    @ApiModelProperty("记录id")
    private Long id;
}
