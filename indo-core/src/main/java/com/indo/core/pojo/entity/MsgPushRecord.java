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
 * 后台推送记录表
 * </p>
 *
 * @author xxx
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MsgPushRecord对象", description="后台推送记录表")
public class MsgPushRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "推送终端: 0 全部 1 ios  2 android")
    private Integer deviceType;

    @ApiModelProperty(value = "是否删除 0 未删除 1 删除")
    private Boolean isDel;

    private String createUser;

    @ApiModelProperty(value = "备注")
    private String remark;


}
