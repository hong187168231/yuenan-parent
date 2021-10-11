package com.live.admin.modules.mem.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;


/**
 * @author
 */
@Data
public class MemBanRebateVoExcel {

    @ExcelProperty(value = "用户名")
    private String account;

    @ExcelProperty(value = "姓名")
    private String realName;

    @ExcelProperty(value = "注册邀请码")
    private String inviteCode;

    @ExcelProperty(value = "上级")
    private String upAccount;

    @ExcelProperty(value = "禁止时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelProperty(value = "注册时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regTime;

    @ExcelProperty(value = "备注")
    private String remark;

}