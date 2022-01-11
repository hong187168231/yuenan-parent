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
 * 系统参数
 * </p>
 *
 * @author xxx
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysParameter对象", description = "系统参数")
public class SysParameter extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "系统参数id")
    @TableId(value = "param_id", type = IdType.AUTO)
    private Long paramId;

    @ApiModelProperty(value = "系统参数代码")
    private String paramCode;

    @ApiModelProperty(value = "系统参数名称")
    private String paramName;

    @ApiModelProperty(value = "系统参数值")
    private String paramValue;

    @ApiModelProperty(value = "是否删除 0 未删除 1 删除")
    private Integer status;

    private Integer sortBy;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "最后修改人")
    private String updateUser;

    @ApiModelProperty(value = "参数说明")
    private String remark;


}
