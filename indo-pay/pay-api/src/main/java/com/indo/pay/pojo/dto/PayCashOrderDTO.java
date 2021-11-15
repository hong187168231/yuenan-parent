package com.indo.pay.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 提取方式配置请求参数
 */

@Data
public class PayCashOrderDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private String orderNo;

    private Integer orderStatus;

    private Integer beginAmount;

    private Integer endAmount;

    private Long userId;

    private Date beginTime;

    private Date endTime;


}
