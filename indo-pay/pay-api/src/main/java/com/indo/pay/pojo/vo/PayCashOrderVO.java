package com.indo.pay.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 提取方式配置返回
 */
@Data
public class PayCashOrderVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long payCashId;

    @ApiModelProperty(value = "提款类型Code")
    private String cashTypeCode;

    @ApiModelProperty(value = "提款类型")
    private String cashName;

    @ApiModelProperty(value = "提款描述")
    private String desc;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "是否正常 0 正常 1 停用")
    private Boolean isDel;

    @ApiModelProperty(value = "排序字段")
    private Integer sortBy;


}
