package com.indo.user.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(value = "stationLetterQueryDTO对象", description = "站内信查询请求参数")
public class StationLetterQueryDTO extends BaseDTO {

    @ApiModelProperty(value = "收件人")
    private String receiver;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

}
