package com.indo.admin.pojo.dto;


import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "代理申请请求参数类")
public class AgentApplyDTO extends BaseDTO {

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applynTime;

}
