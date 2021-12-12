package com.indo.user.pojo.req.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "代理申请参数类")
public class MemAgentApplyReq {

    @ApiModelProperty(value = "用户id", required = true)
    private Long memId;

    @ApiModelProperty(value = "用户名", required = true)
    private String account;

    @ApiModelProperty(value = "登录密码", required = true)
    private String password;

    @ApiModelProperty(value = "真实姓名", required = true)
    private String realName;

    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;
}
