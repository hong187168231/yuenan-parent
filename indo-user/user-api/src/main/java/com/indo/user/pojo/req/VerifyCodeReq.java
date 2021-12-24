package com.indo.user.pojo.req;

import com.indo.common.enums.VerifCodeTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Data
@ApiModel(value = "注册请求参数类")
public class VerifyCodeReq {

    @ApiModelProperty(value = "国家区号", required = true)
    @NotBlank
    private String areaCode;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank
    private String phone;

    @ApiModelProperty(value = "验证码类型", required = true)
    @NotEmpty
    private VerifCodeTypeEnum verifCodeTypeEnum;


    // 倒计时 秒
    @ApiModelProperty(hidden = true)
    private Integer countDown;

    // 倒计时 秒
    @ApiModelProperty(hidden = true)
    private Integer sendType;

    public Integer getSendType() {
        if(verifCodeTypeEnum ==null){
            return null;
        }
        return verifCodeTypeEnum.getCode();
    }
}
