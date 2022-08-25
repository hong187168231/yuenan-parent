package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemBetInfoDTO extends BaseDTO {
    @ApiModelProperty(value = "会员账号")
    private String memAccount;
}
