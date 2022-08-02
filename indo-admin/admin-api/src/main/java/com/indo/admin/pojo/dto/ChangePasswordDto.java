package com.indo.admin.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordDto {

    @ApiModelProperty(value = "旧密码(MD5)")
    private String usedPassword;

    @ApiModelProperty(value = "新密码(MD5)")
    private String newPassword;
}
