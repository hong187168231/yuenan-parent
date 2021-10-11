package com.live.pay.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 支付层级配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayGroupConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long payGroupId;

    @ApiModelProperty(value = "分层名称")
    private String groupName;

    @ApiModelProperty(value = "分层值")
    private String groupVal;

    @ApiModelProperty(value = "单笔最大充值金额")
    private String singleMaxAmount;

    @ApiModelProperty(value = "当日最大充值金额")
    private String todayMaxAmount;

    @ApiModelProperty(value = "单笔最大提现金额")
    private String singleCashMaxAmount;

    @ApiModelProperty(value = "当日最大提现金额")
    private String todayCashMaxAmount;

    @ApiModelProperty(value = "当日最大提现次数")
    private Integer todayCashMaxNum;

    @ApiModelProperty(value = "当日最大连续未支付")
    private String todayMaxNotPay;

    @ApiModelProperty(value = "是否正常 0 正常 1 停用")
    private Boolean isDel;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "最后更新人")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

}
