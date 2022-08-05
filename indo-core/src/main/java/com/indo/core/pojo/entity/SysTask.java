package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 任务表
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysTask对象", description="任务表")
public class SysTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务类型id")
    private Integer typeId;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "任务详情")
    private String details;

    @ApiModelProperty(value = "任务开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "任务结束时间(为空则永久)")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "任务图片")
    private String picture;

    @ApiModelProperty(value = "状态：0上架，1下架")
    private Integer states;

    @ApiModelProperty(value = "创建人(新增修改勿传)")
    private String createUser;

    @ApiModelProperty(value = "任务奖金")
    private BigDecimal bonusAmount;


}
