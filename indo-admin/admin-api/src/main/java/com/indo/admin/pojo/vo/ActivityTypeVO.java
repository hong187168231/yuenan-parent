package com.indo.admin.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActivityTypeVO {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String image;

    @ApiModelProperty(value = "内容")
    private String skipUrl;

    @ApiModelProperty(value = "发送人id")
    private String content;

    @ApiModelProperty(value = "状态 0 下架1 上架")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

}
