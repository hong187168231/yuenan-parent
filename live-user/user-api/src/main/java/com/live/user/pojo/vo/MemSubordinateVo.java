package com.live.user.pojo.vo;

import com.live.user.pojo.entity.MemSubordinate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemSubordinateVo extends MemSubordinate {

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "类别")
    private Integer IdentityType;

    @ApiModelProperty(value = "注册人数")
    private Integer register;

}