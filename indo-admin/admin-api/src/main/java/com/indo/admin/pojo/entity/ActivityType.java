package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 活动类型表
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("act_activity_type")
@ApiModel(value="ActivityType对象", description="活动类型表")
public class ActivityType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "activity_type_id", type = IdType.AUTO)
    private Long activityTypeId;

    @ApiModelProperty(value = "活动类型名称")
    private String activityTypeName;

    @ApiModelProperty(value = "活动数量")
    private Integer activityNum;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "修改人")
    private String updateUser;

    @ApiModelProperty(value = "备注")
    private String remark;


}
