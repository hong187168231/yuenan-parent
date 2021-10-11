package com.live.user.pojo.vo;

import com.live.user.pojo.entity.ManualDepositWithDraw;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 人工提取返回对象
 */
@Data
public class ManualDepositWithDrawVO extends ManualDepositWithDraw {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "用户名")
    private String account;

    @ApiModelProperty(value = "用户等级")
    private Integer level;

    @ApiModelProperty(value = "会员类型 0 普通会员 1 代理会员 2带玩会员")
    private Integer identityType;



}
