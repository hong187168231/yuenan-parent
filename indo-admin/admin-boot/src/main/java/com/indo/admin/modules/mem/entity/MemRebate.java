package com.indo.admin.modules.mem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 返点配置表
 * </p>
 *
 * @author xxx
 * @since 2021-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MemRebate对象", description="返点配置表")
public class MemRebate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "返点值")
    private String rebateValue;


}
