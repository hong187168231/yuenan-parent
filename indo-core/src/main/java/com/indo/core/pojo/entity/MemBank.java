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
 * 银行信息配置表
 * </p>
 *
 * @author xxx
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MemBank对象", description="银行信息配置表")
public class MemBank extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long payBankId;

    private Long userId;

    @ApiModelProperty(value = "银行名称")
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
