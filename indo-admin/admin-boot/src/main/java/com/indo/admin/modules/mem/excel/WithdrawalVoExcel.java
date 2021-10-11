package com.indo.admin.modules.mem.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WithdrawalVoExcel {

    @ExcelProperty(value = "订单id")
    private Long orderId;

    @ExcelProperty(value = "用户ID")
    private Long memId;

    @ExcelProperty(value = "提现金额")
    private BigDecimal amount;

    @ExcelProperty(value = "手续费")
    private BigDecimal serviceFee;

    @ExcelProperty(value = "用户绑定银行id")
    private Long bankRelationId;

    @ExcelProperty(value = "提现账户")
    private String withdrawalAccount;

    @ExcelProperty(value = "提现方式：1 支付宝 2 微信 3 银行卡")
    private Integer withdrawalType;

    @ExcelProperty(value = "申请时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @ExcelProperty(value = "状态：0 提现中 1 提现拒绝 2提现通过")
    private Integer status;

    @ExcelProperty(value = "审核时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examineTime;

    @ExcelProperty(value = "审核说明")
    private String reason;

    @ExcelProperty(value = "出款第三方")
    private String disbursement;

    @ExcelProperty(value = "操作人")
    private String operator;

    @ExcelProperty(value = "是否删除 0 未删除 1 删除")
    private Boolean isDel;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "用户名")
    private String account;

    @ExcelProperty(value = "真实姓名")
    private String realName;

    @ExcelProperty(value = "层级id")
    private Long hierarchyId;

    @ExcelProperty(value = "银行名称")
    private String bankName;

    @ExcelProperty(value = "卡号")
    private String bankCard;

    @ExcelProperty(value = "开户地址")
    private String accountOpeningAddress;

    @ExcelProperty(value = "收款人")
    private String payee;

}
