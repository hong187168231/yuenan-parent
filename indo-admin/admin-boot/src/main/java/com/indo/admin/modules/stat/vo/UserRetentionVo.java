package com.indo.admin.modules.stat.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRetentionVo {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "新增用户数")
    private Integer newUsers;

    @ApiModelProperty(value = "次日留存")
    private Double nextDay;

    @ApiModelProperty(value = "3日留存")
    private Double threeDay;

    @ApiModelProperty(value = "7日留存")
    private Double sevevDay;

    @ApiModelProperty(value = "30日留存")
    private Double thirtyDay;
}
