package com.indo.admin.pojo.vo.stat;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserRetentionVo {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date currentDate = new Date();

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
