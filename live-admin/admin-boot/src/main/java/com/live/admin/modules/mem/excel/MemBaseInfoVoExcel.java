package com.live.admin.modules.mem.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Mr.liu
 * @Date: 2021/9/1 11:57
 * @Version: 1.0.0
 * @Desc:
 */
@Data
public class MemBaseInfoVoExcel{

    @ExcelProperty(value = "ID", index = 0)
    private Long id;

    @ExcelProperty(value = "会员类型", index = 1)
    @ColumnWidth(value = 15)
    private Integer IdentityType;

    @ExcelProperty(value = "用户名", index = 2)
    @ColumnWidth(value = 10)
    private String account;

    @ExcelProperty(value = "用户姓名", index = 3)
    @ColumnWidth(value = 15)
    private String realName;

    @ExcelProperty(value = "用户昵称", index = 4)
    @ColumnWidth(value = 15)
    private String nickName;

    @ExcelProperty(value = "用户电话", index = 5)
    @ColumnWidth(value = 15)
    private String mobile;

    @ExcelProperty(value = "邮箱", index = 6)
    @ColumnWidth(value = 10)
    private String email;

    @ExcelProperty(value = "QQ号", index = 7)
    @ColumnWidth(value = 10)
    private String qq;

    @ExcelProperty(value = "微信号", index = 8)
    @ColumnWidth(value = 10)
    private String wechat;

    @ExcelProperty(value = "用户邀请码", index = 9)
    @ColumnWidth(value = 20)
    private String inviteCode;

    @ExcelProperty(value = "提现费率", index = 10)
    @ColumnWidth(value = 15)
    private BigDecimal cashRate;

    @ExcelProperty(value = "第三方反水状态", index = 11)
    @ColumnWidth(value = 20)
    private Integer rebateStatus;

    @ExcelProperty(value = "能否修改下级返水", index = 12)
    @ColumnWidth(value = 25)
    private Integer belowOddsStatus;

    @ExcelProperty(value = "成长值和等级更新状态", index = 13)
    @ColumnWidth(value = 30)
    private Integer growthStatus;

    @ExcelProperty(value = "当天最大提款次数", index = 14)
    @ColumnWidth(value = 25)
    private Integer maxCashNum;

    @ExcelProperty(value = "今日密码错误次数", index = 15)
    @ColumnWidth(value = 25)
    private Integer passErrorNum;

    @ExcelProperty(value = "头像地址", index = 16)
    @ColumnWidth(value = 15)
    private String headUrl;

    @ExcelProperty(value = "层级id", index = 17)
    @ColumnWidth(value = 10)
    private Long hierarchyId;

    @ExcelProperty(value = "等级id", index = 18)
    @ColumnWidth(value = 10)
    private Long levelId;

    @ExcelProperty(value = "用户状态", index = 19)
    @ColumnWidth(value = 15)
    private Boolean status;

    @ExcelProperty(value = "会员积分", index = 20)
    @ColumnWidth(value = 15)
    private Integer point;

    @ExcelProperty(value = "注册来源", index = 21)
    @ColumnWidth(value = 15)
    private Integer source;

    @ExcelProperty(value = "余额", index = 22)
    @ColumnWidth(value = 10)
    private Long balance;

    @ExcelProperty(value = "设备号", index = 23)
    @ColumnWidth(value = 10)
    private String deviceCode;

    @ExcelProperty(value = "最后登录时间", index = 24)
    @ColumnWidth(value = 25)
    private Date lastLoginTime;

    @ExcelProperty(value = "上级代理账号", index = 25)
    @ColumnWidth(value = 25)
    private String superiorAgent;

    @ExcelProperty(value = "下级数", index = 26)
    @ColumnWidth(value = 10)
    private Integer subordinateNum;

    @ExcelProperty(value = "团队数", index = 27)
    @ColumnWidth(value = 10)
    private Integer teamNum;

    @ExcelProperty(value = "注册时间", index = 28)
    @ColumnWidth(value = 15)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelProperty(value = "离开天数", index = 29)
    @ColumnWidth(value = 15)
    private Integer leaveDays;

    @ExcelProperty(value = "首充时间", index = 30)
    @ColumnWidth(value = 15)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date firstRechargeTime;

    @ExcelProperty(value = "余额宝", index = 31)
    @ColumnWidth(value = 10)
    private BigDecimal yuebao;

    @ExcelProperty(value = "余额宝利息", index = 32)
    @ColumnWidth(value = 20)
    private BigDecimal yuebaoInterest;

    @ExcelProperty(value = "基金", index = 33)
    @ColumnWidth(value = 10)
    private BigDecimal fund;

    @ExcelProperty(value = "基金利息", index = 34)
    @ColumnWidth(value = 15)
    private BigDecimal fundInterest;

    @ExcelProperty(value = "存款总额", index = 35)
    @ColumnWidth(value = 15)
    private BigDecimal depositTotal;

    @ExcelProperty(value = "取款总额", index = 36)
    @ColumnWidth(value = 15)
    private BigDecimal withdrawalTotal;

    @ExcelProperty(value = "存款次数", index = 37)
    @ColumnWidth(value = 15)
    private Integer depositNum;

    @ExcelProperty(value = "取款次数", index = 38)
    @ColumnWidth(value = 15)
    private Integer withdrawalNum;

    @ExcelProperty(value = "代理等级", index = 39)
    @ColumnWidth(value = 15)
    private Integer hierarchy;

}
