package com.indo.admin.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BeforeDayDTO {

    private Long parentId;
    private Integer memLevel;
    private String realName;
    private String superior;
    private Integer teamNum;
    private String account;
    private BigDecimal realBetAmount;

}
