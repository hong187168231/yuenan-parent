package com.indo.admin.modules.report.excel;

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
public class MemReportVoExcel {
    @ExcelProperty(value = "用户ID")
    private Long id;

    @ExcelProperty(value = "会员类型 0 普通会员 1 代理会员")
    private Integer IdentityType;

    @ExcelProperty(value = "用户名")
    private String account;

    @ExcelProperty(value = "用户姓名")
    private String realName;

    @ExcelProperty(value = "等级id")
    private Long levelId;

    @ExcelProperty(value = "上级代理账号")
    private String superiorAgent;

    @ExcelProperty(value = "余额")
    private Long balance;

    @ExcelProperty(value = "盈利")
    private BigDecimal profit;

    @ExcelProperty(value = "盈利")
    private BigDecimal profitRatio;

    @ExcelProperty(value = "充值金额")
    private BigDecimal rechargeAmount;

    @ExcelProperty(value = "充值次数")
    private Integer rechargeCount;

    @ExcelProperty(value = "提现金额")
    private BigDecimal withdrawalAmount;

    @ExcelProperty(value = "活动礼金")
    private BigDecimal activityAmount;

    @ExcelProperty(value = "返点金额")
    private BigDecimal rebateAmount;

    @ExcelProperty(value = "注册时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date CreateTime;

}
