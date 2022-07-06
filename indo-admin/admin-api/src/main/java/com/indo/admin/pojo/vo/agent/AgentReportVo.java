package com.indo.admin.pojo.vo.agent;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AgentReportVo {
    @ApiModelProperty(value = "日期")
    private String dataTime;

    @ApiModelProperty(value = "代理总人数")
    private Integer agentTotalQuantity;

    @ApiModelProperty(value = "代理新增人数")
    private Integer agentNewQuantity;

    @ApiModelProperty(value = "团队投注金额")
    private BigDecimal teamAmout;

    @ApiModelProperty(value = "代理返点总金额")
    private BigDecimal totalRebateAmount;

    @ApiModelProperty(value = "代理返点提现总金额")
    private BigDecimal totalWithdrawalAmount;
}
