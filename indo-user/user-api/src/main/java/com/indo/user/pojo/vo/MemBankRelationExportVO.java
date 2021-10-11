package com.indo.user.pojo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MemBankRelationExportVO {
    @ExcelProperty(value = "用户名")
    private String account;

    @ExcelProperty(value = "姓名")
    private String realName;

    @ExcelProperty(value = "银行")
    private String bankName;

    @ExcelProperty(value = "银行卡号")
    private String bankCard;

    @ExcelProperty(value = "开户地址")
    private String accountOpeningAddress;

    @ExcelProperty(value = "状态")
    private int status;

    @ExcelProperty(value = "绑定时间")
    private Date createTime;
}
