package com.indo.user.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行支付配置返回
 */
@Data
public class PayBankVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "银行卡id")
    private Long bankId;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

}
