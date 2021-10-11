package com.indo.pay.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 在线支付配置返回
 */
@Data
public class PayOnlineConfigVO extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long payOnlineId;

    @ApiModelProperty(value = "支付名称")
    private String payName;

    @ApiModelProperty(value = "支付类型")
    private String channeType;

    @ApiModelProperty(value = "商户号")
    private String merchantNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "充值层级")
    private String groupName;

    @ApiModelProperty(value = "是否正常 0 正常 1 停用")
    private Boolean isDel;


}
