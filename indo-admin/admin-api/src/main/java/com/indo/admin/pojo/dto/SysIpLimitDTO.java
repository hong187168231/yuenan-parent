package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysIpLimitDTO extends BaseDTO {
    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "类型：1移动端黑名单，2后管白名单")
    private Integer types;
}
