package com.indo.user.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author puff
 * @since 2021-08-31
 */
@Data
public class MemBankVo {


    @ApiModelProperty(value = "主键")
    private Long memBankId;

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "银行卡用户名")
    private String userName;

    @ApiModelProperty(value = "银行id")
    private Long bankId;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "预留手机号")
    private String phone;


}
