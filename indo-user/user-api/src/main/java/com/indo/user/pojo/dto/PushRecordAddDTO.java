package com.indo.user.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PushRecordAddDTO extends BaseDTO {

    /**
     * 标题
     */
    @NotNull(message = "标题不能为空")
    private String title;

    /**
     * 内容
     */
    @NotNull(message = "内容不能为空")
    private String content;

    /**
     * 推送终端: 0 全部 1 ios  2 android
     */
    @NotNull(message = "推送终端类型不能为空")
    private Integer deviceType;

}
