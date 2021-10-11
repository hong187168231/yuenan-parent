package com.indo.user.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MsgPlatformAnnouncementDTO {

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "分页数")
    private Integer limit;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    @NotNull(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "内容")
    @NotNull(message = "内容不能为空")
    private String content;

    @ApiModelProperty(value = "开发等级")
    @NotNull(message = "开发等级不能为空")
    private List<String> levelIds;

    @ApiModelProperty(value = "开放终端: 0 PC 1 Mobile")
    @NotNull(message = "开放终端不能为空")
    private Integer type;
}
