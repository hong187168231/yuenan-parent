package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 活动记录表
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("act_activity_record")
@ApiModel(value="ActivityRecord对象", description="活动记录表")
public class ActivityRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "activity_id", type = IdType.AUTO)
    private Long activityId;

    @ApiModelProperty(value = "活动类型id")
    private Long activityTypeId;

    @ApiModelProperty(value = "活动图片")
    private String activityImage;

    @ApiModelProperty(value = "状态 0 下架1 上架")
    private Integer status;

    private String activityDetail;

    @ApiModelProperty(value = "是否永久活动 0否 1 是")
    private Integer isPer;

    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "备注")
    private String remark;


}
