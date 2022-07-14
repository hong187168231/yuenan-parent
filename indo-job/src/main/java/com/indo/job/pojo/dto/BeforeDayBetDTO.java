package com.indo.job.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BeforeDayBetDTO {

    private Long parentId;
    private Integer memLevel;
    private String realName;
    private String superior;
    private Integer teamNum;
    private String account;
    private BigDecimal realBetAmount;

}
