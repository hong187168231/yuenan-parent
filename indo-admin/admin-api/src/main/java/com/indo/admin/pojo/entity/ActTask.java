package com.indo.admin.pojo.entity;

import com.indo.common.pojo.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 任务记录表
 * </p>
 *
 * @author xxx
 * @since 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ActTask对象", description="任务记录表")
public class ActTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "任务类型id")
    private Long taskTypeId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务图片地址")
    private String taskImageUrl;

    @ApiModelProperty(value = "状态 0 下架 1 上架  2 已过期")
    private Integer status;

    @ApiModelProperty(value = "任务详情")
    private String content;

    @ApiModelProperty(value = "任务奖励")
    private String reward;

    @ApiModelProperty(value = "是否永久任务 0否 1 是")
    private Integer isPer;

    @ApiModelProperty(value = "任务开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "任务结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "备注")
    private String remark;


}
