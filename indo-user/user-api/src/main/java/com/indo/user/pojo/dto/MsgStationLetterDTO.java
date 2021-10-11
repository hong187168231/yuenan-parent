package com.indo.user.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MsgStationLetterDTO {
    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "分页数")
    private Integer limit;

    @ApiModelProperty(value = "收件人")
    private String receiverName;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    @NotNull(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "内容")
    @NotNull(message = "内容不能为空")
    private String content;

    @ApiModelProperty(value = "发送人id")
    @NotNull(message = "发送人id不能为空")
    private Long sendMemId;

    @ApiModelProperty(value = "接收人")
    @NotNull(message = "接收人不能为空")
    private List<String> receiver;

    @ApiModelProperty(value = "发送类型: 1 按收件人发送 2 按会员等级发送 3 按支付层级发送")
    @NotNull(message = "发送类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "创建起始日期")
    private String beginTime;

    @ApiModelProperty(value = "创建结束日期")
    private String endTime;
}
