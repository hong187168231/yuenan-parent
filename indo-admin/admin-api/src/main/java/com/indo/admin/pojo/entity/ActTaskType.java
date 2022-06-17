package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.indo.common.pojo.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 任务类型表
 * </p>
 *
 * @author xxx
 * @since 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ActTaskType对象", description="任务类型表")
public class ActTaskType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "活动类型名称")
    private String typeName;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "任务数量")
    private Integer taskNum;


}
