package com.indo.admin.pojo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.base.BaseDTO;
import com.indo.common.pojo.entity.BaseEntity;
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
public class SysParameterQueryReq extends BaseDTO {

    @ApiModelProperty(value = "系统参数编码")
    private String paramCode;

    @ApiModelProperty(value = "系统参数名称")
    private String paramName;

}
