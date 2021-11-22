package com.indo.user.pojo.req.mem;

import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询个人消息参数类")
public class MemNoticePageReq extends QueryParam {

    @ApiModelProperty(value = "用户id", required = true)
    private String memId;
}
