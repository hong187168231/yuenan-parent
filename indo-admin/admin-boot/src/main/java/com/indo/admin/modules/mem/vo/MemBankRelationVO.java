package com.indo.admin.modules.mem.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "银行名称")
    private Long bankName;

    @ApiModelProperty(value = "银行卡号")
    private String bankCard;

    @ApiModelProperty(value = "开户地址")
    private String accountOpeningAddress;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "预留手机号")
    private String phone;

    @ApiModelProperty(value = "状态：0 禁用 1 开启")
    private Boolean status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
