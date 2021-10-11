package com.live.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author puff
 * @since 2021-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemBank extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "银行名称")
    @NotNull(message = "银行名称不能为空")
    private String name;

    @ApiModelProperty(value = "银行简称：eg:ICBC")
    private String code;

    @ApiModelProperty(value = "银行icon(小)")
    private String icon;

    @ApiModelProperty(value = "银行icon(大)")
    private String iconDetail;

    @ApiModelProperty(value = "是否删除 0 未删除 1 删除")
    private Boolean isDel;

    @ApiModelProperty(value = "备注")
    private String remark;


}
