package com.indo.core.pojo.req;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 系统参数
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@Data
public class SysParameterQueryReq extends BaseDTO {

    @ApiModelProperty(value = "系统参数编码")
    private String paramCode;

    @ApiModelProperty(value = "系统参数名称")
    private String paramName;

}
