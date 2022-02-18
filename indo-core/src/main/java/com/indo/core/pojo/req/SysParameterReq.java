package com.indo.core.pojo.req;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统参数
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysParameterReq extends BaseDTO {

    @ApiModelProperty(value = "系统参数id")
    private Long paramId;

    @ApiModelProperty(value = "系统参数代码")
    private String paramCode;

    @ApiModelProperty(value = "系统参数名称")
    private String paramName;

    @ApiModelProperty(value = "系统参数值")
    private String paramValue;

    @ApiModelProperty(value = "是否删除 0关闭 1 开启")
    private Integer status;

    @ApiModelProperty(value = "参数说明")
    private String remark;



}
