package com.indo.admin.modules.mem.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel
public class AgentVo {

    @ApiModelProperty(value = "代理编号")
    private Long agentId;

    @ApiModelProperty(value = "用户ID")
    private Long memId;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "团队数")
    private Integer teamNum;

    @ApiModelProperty(value = "上级代理")
    private String superior;

    @ApiModelProperty(value = "总存款")
    private BigDecimal totalDeposit;

    @ApiModelProperty(value = "总取款")
    private BigDecimal totalWithdraw;


}
