package com.indo.admin.pojo.req.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @des:新增会员请求参数
 * @Author: kevin
 */
@Data
@ApiModel
public class MemAddReq {

    @ApiModelProperty("会员账号，4-15位只能包含数字和字母")
    private String account;

    @ApiModelProperty("上级账号")
    private String superAccno;

    @ApiModelProperty("账户类型，1-玩家 2-代理")
    private Integer accType = 1;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("等级")
    private Integer memLevel;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("余额")
    private BigDecimal balance;
}
