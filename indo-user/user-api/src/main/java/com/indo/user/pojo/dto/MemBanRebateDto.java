package com.indo.user.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName : MemBanRebateReq
 * @Description : 禁止返点请求参数类
 * @Author :
 * @Date: 2020-09-25 16:16
 */
@Data
@ApiModel(value = "禁止返点请求参数类")
public class MemBanRebateDto {

    @ApiModelProperty(value = "条数")
    private Integer limit;
    @ApiModelProperty(value = "页数")
    private Integer page;
    @ApiModelProperty(value = "级别")
    private Long levelId;
    @ApiModelProperty(value = "账号")
    private List<String> accounts;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "ids")
    private List<Long> ids;
}
