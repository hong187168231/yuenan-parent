package com.live.pay.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 提取方式配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayCashConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long payCashId;

    @ApiModelProperty(value = "提款人")
    private String cashName;

    @ApiModelProperty(value = "提款类型code")
    private String cashTypeCode;

    @ApiModelProperty(value = "提款描述")
    private String desc;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "提款服务手续费百分比")
    private Float cashFee;

    @ApiModelProperty(value = "提款绑定账号数量")
    private Integer bindAccountNum;

    @ApiModelProperty(value = "最小提款金额")
    private Integer minCashAmount;

    @ApiModelProperty(value = "最大提款金额")
    private Integer maxCashAmount;

    @ApiModelProperty(value = "是否正常 0 正常 1 停用")
    private Integer isDel;

    @ApiModelProperty(value = "排序字段")
    private Integer sortBy;


}
