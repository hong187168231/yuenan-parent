package com.indo.admin.pojo.vo.mem;

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
public class MemBankVO {

    @ApiModelProperty(value = "主键")
    @TableId(value = "memBankId", type = IdType.AUTO)
    private Long memBankId;

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "银行卡用户名")
    private String userName;

    @ApiModelProperty(value = "银行卡用户名")
    private String bankName;

    @ApiModelProperty(value = "银行id")
    private Long bankId;

    @ApiModelProperty(value = "银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "支行")
    private String bankBranch;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "ifsc")
    private String ifsc;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "预留手机号")
    private String phone;

    @ApiModelProperty(value = "状态：0 开启 1 禁用")
    private Integer status;

    @ApiModelProperty(value = "添加时间")
    private String createTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
