package com.indo.user.pojo.dto;


import com.indo.common.base.BaseDTO;
import com.indo.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "代理报表请求参数类")
public class AgentDTO extends BaseDTO {

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "代理id")
    private Long agentId;


}
