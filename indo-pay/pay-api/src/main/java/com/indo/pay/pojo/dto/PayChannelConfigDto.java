package com.indo.pay.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import com.indo.pay.pojo.entity.PayChannelConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付渠道置请求参数
 */
@Data
public class PayChannelConfigDto extends PayChannelConfig {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "分页数")
    private Integer limit;
}
