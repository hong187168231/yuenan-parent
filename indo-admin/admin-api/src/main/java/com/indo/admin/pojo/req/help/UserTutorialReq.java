package com.indo.admin.pojo.req.help;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.base.BaseDTO;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用戶教程表
 *
 * @author teman@cg.app
 * @since 1.0.0
 */
@Data
@ApiModel
public class UserTutorialReq extends BaseDTO {

	private String content;

}
