package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 会员账变记录表
 * </p>
 *
 * @author xxx
 * @since 2021-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mem_gold_change")
@ApiModel(value = "GoldChange对象", description = "会员账变记录表")
public class MemGoldChange extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账表id")
    @TableId(value = "gold_changId _id", type = IdType.AUTO)
    private Long goldChangId;

    @ApiModelProperty(value = "相关id 如充值订单id 、彩票派獎id(ksorderid)  代理結算id")
    @TableField("refId")
    private Long refId;

    @ApiModelProperty(value = "账变流水号")
    private String serialNo;

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "变动类型 1充值 2充值附赠 3提現 5投注 6代理结算 等等其他类型-需要的时候进行补充")
    private Integer changeType;

    @ApiModelProperty(value = "交易类型 1-收入, 2-支出")
    private Integer tradingType;

    @ApiModelProperty(value = "变动前金额")
    private BigDecimal beforeAmount;

    @ApiModelProperty(value = "变动后金额")
    private BigDecimal afterAmount;

    @ApiModelProperty(value = "账变金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "变动前打码量")
    private BigDecimal beforeCodeAmount;

    @ApiModelProperty(value = "变动后打码量")
    private BigDecimal afterCodeAmount;

    @ApiModelProperty(value = "操作说明")
    private String opNote;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;


}
