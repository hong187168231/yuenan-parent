package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ActivityTypeDTO extends BaseDTO {

    @ApiModelProperty(value = "活动类型名称")
    private String activityTypeName;

}
