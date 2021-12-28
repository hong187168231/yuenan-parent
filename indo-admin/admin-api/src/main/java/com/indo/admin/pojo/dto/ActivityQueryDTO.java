package com.indo.admin.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ActivityQueryDTO extends BaseDTO {

    @ApiModelProperty(value = "活动名称")
    private String actName;

}
