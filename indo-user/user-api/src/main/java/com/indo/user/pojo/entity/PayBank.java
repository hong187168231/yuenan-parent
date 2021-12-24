package com.indo.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 支付银行表
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PayBank对象", description="支付银行表")
public class PayBank extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "bank_id", type = IdType.AUTO)
    private Long bankId;

    @ApiModelProperty(value = "银行编码")
    private String bankCode;

    @ApiModelProperty(value = "银行名称")
    private String bankName;


}
