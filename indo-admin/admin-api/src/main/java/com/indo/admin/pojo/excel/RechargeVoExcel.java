package com.indo.admin.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RechargeVoExcel {

    @ExcelProperty(value = "订单id")
    private Long orderId;

    @ExcelProperty(value = "用户名")
    private String account;

    @ExcelProperty(value = "真实姓名")
    private String realName;

    @ExcelProperty(value = "层级id")
    private Long hierarchyId;

    @ExcelProperty(value = "充值金额")
    private BigDecimal amount;

    @ExcelProperty(value = "付款方式")
    private Integer paidMethod;

    @ExcelProperty(value = "充值状态")
    private Integer tradeStatus;

    @ExcelProperty(value = "操作人")
    private String operator;

    @ExcelProperty(value = "提交时间", index = 9)
    @ColumnWidth(value = 15)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelProperty(value = "备注")
    private String remark;

}
