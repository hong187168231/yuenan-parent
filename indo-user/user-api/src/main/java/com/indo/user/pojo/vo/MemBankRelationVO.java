package com.indo.user.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: mzh
 * @Date: 2021/9/01 16:05
 * @Version: 1.0.0
 * @Desc: 用户银行卡信息返回类
 */
@Data
public class MemBankRelationVO {

    @ApiModelProperty(value = "用户银行卡关联Id")
    private Long memBankRelationId;

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "用户名")
    private String account;

    @ApiModelProperty(value = "用户真实名称")
    private String realName;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行id")
    private Long bankId;

    @ApiModelProperty(value = "银行卡号")
    private String bankCard;

    @ApiModelProperty(value = "开户地址")
    private String accountOpeningAddress;

    @ApiModelProperty(value = "银行卡状态")
    private int status;

    @ApiModelProperty(value = "绑定银行卡时间")
    private Date createTime;
}
