package com.live.admin.modules.report.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 返回参数
 */
@Data
public class AgentReportVoExcel {
    @ExcelProperty(value = "用户ID")
    private Long id;

    @ExcelProperty(value = "会员类型 0 普通会员 1 代理会员")
    private Integer IdentityType;

    @ExcelProperty(value = "代理账号")
    private String account;

    @ExcelProperty(value = "注册人数")
    private Integer register;

    @ExcelProperty(value = "首充人数")
    private Integer levelId;

    @ExcelProperty(value = "代理返点")
    private Integer superiorAgent;

    @ExcelProperty(value = "团队盈利")
    private BigDecimal profit;

    @ExcelProperty(value = "充值金额")
    private BigDecimal rechargeAmount;

    @ExcelProperty(value = "提现金额")
    private BigDecimal withdrawalAmount;

    @ExcelProperty(value = "注册时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date CreateTime;

}