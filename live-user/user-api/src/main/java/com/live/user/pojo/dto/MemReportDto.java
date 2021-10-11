package com.live.user.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "会员报表请求参数类")
public class MemReportDto {

    @ApiModelProperty(value = "用户名")
    private String account;

    @ApiModelProperty(value = "用户姓名")
    private String realName;

    @ApiModelProperty(value = "等级id")
    private Long levelId;

    @ApiModelProperty(value = "上级代理账号")
    private String superiorAgent;

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
