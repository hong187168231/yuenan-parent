package com.indo.pay.pojo.dto;

import com.indo.pay.pojo.entity.PayOnlineConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 在线支付配置请求参数
 */
@Data
public class PayOnlineConfigDto extends PayOnlineConfig {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "分页数")
    private Integer limit;
}
