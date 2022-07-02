package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayRechargeReportDTO extends BaseDTO {
    @ApiModelProperty(value = "开始时间(年-月-日)")
    private String beginTime;

    @ApiModelProperty(value = "结束时间(年-月-日)")
    private String endTime;
}
