package com.indo.user.pojo.vo.help;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author puff
 * @since 2021-08-31
 */
@Data
public class HelpVo {

    @ApiModelProperty(value = "tg")
    private String tg;

    @ApiModelProperty(value = "邮箱")
    private String email;


}
