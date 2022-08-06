package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
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
 * @since 2022-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysTaskType对象", description="任务类型表")
public class SysTaskType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "创建人(新增修改勿传)")
    private String createUser;

    @ApiModelProperty(value = "任务数量(新增修改勿传)")
    @TableField(exist = false)
    private Integer taskNum;


}
