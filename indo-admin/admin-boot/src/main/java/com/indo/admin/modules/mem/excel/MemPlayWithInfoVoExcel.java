package com.indo.admin.modules.mem.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Mr.liu
 * @Date: 2021/9/1 11:57
 * @Version: 1.0.0
 * @Desc:
 */
@Data
public class MemPlayWithInfoVoExcel {

    @ExcelProperty(value = "ID", index = 0)
    private Long id;

    @ExcelProperty(value = "注册来源", index = 1)
    @ColumnWidth(value = 15)
    private Integer source;

    @ExcelProperty(value = "用户名", index = 2)
    @ColumnWidth(value = 10)
    private String account;

    @ExcelProperty(value = "姓名", index = 3)
    @ColumnWidth(value = 15)
    private String realName;

    @ExcelProperty(value = "用户邀请码", index = 4)
    @ColumnWidth(value = 20)
    private String inviteCode;

    @ExcelProperty(value = "上级", index = 5)
    @ColumnWidth(value = 25)
    private String superiorAgent;

    @ExcelProperty(value = "等级id", index = 6)
    @ColumnWidth(value = 10)
    private Long levelId;

    @ExcelProperty(value = "用户状态", index = 7)
    @ColumnWidth(value = 15)
    private Boolean status;

    @ExcelProperty(value = "层级id", index = 8)
    @ColumnWidth(value = 10)
    private Long hierarchyId;

    @ExcelProperty(value = "注册时间", index = 9)
    @ColumnWidth(value = 15)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
