package com.indo.admin.modules.mem.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * @author
 */
@Data
public class MemSubordinateVoExcel {

    @ExcelProperty(value = "邀请码")
    private String inviteCode;

    @ExcelProperty(value = "账号")
    private String account;

    @ExcelProperty(value = "类别")
    private Integer IdentityType;

    @ExcelProperty(value = "注册人数")
    private Integer register;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "添加时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}