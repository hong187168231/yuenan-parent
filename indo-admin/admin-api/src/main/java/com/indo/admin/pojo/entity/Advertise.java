package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 广告表
 * </p>
 *
 * @author xxx
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ade_advertise")
@ApiModel(value="Advertise对象", description="广告表")
public class Advertise extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "ade_id", type = IdType.AUTO)
    private Long adeId;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "跳转地址")
    private String skipUrl;

    @ApiModelProperty(value = "内容详情")
    private String content;

    @ApiModelProperty(value = "状态 0 下架1 上架")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;


}
