package com.indo.admin.pojo.vo.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
public class ApplyStatusVO implements Serializable {


    @ApiModelProperty(value = "代理申请状态 状态0 待审核 1 已通过，2 拒绝, -1 暂未申请")
    private Integer status;


}
