package com.indo.admin.pojo.vo.agent;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author xxx
 * @since 2022-01-10
 */
@Data
@ApiModel(value = "agentRebateRecord对象", description = "")
public class AgentRebateRecordVO implements Serializable {


    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "会员等级")
    private Integer memLevel;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "上级代理")
    private String superior;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "昨日结余")
    private BigDecimal yesterdayRemain;

    @ApiModelProperty(value = "返点金额")
    private BigDecimal rebateAmout;

    @ApiModelProperty(value = "累计投注")
    private BigDecimal totalBet;

    @ApiModelProperty(value = "发放人")
    private String createUser;


}
