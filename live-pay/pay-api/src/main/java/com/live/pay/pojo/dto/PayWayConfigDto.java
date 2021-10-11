package com.live.pay.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import com.live.pay.pojo.entity.PayWayConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 支付方式配置请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayWayConfigDto extends PayWayConfig {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "分页数")
    private Integer limit;

}
