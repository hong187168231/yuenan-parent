package com.indo.user.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "代理报表请求参数类")
public class AgentReportDto {

    @ApiModelProperty(value = "用户名")
    private String account;

    @ApiModelProperty(value = "类别")
    private Integer IdentityType;

    @ApiModelProperty(value = "起始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "页数")
    private Integer page;

    @ApiModelProperty(value = "条数")
    private Integer limit;

    @ApiModelProperty(value = "ids")
    private List<Long> ids;
}
