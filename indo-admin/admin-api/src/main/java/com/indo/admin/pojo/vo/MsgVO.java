package com.indo.admin.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class MsgVO implements Serializable {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
