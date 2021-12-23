package com.indo.user.pojo.vo.sms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理报表返回参数
 */
@Data
public class SmsCodeVo {


    @ApiModelProperty(value = "短信代码")
    private String smsCode;

    @ApiModelProperty(value = "国家")
    private String country;


}
