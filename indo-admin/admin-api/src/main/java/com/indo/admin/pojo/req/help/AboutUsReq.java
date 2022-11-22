package com.indo.admin.pojo.req.help;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 用戶教程表
 *
 * @author teman@cg.app
 * @since 1.0.0
 */
@Data
@ApiModel
public class AboutUsReq extends BaseDTO {

	private String content;

}
