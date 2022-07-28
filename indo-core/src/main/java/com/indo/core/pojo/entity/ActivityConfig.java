package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 活动配置表
 * </p>
 *
 * @author xxx
 * @since 2022-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ActivityConfig对象", description="活动配置表")
public class ActivityConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "活动类型：1签到，2借呗")
    private Integer types;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动配置信息JSON")
    private String configInfo;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;


}
