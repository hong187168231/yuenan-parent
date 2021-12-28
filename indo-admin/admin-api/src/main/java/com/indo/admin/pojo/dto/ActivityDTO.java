package com.indo.admin.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ActivityDTO extends BaseDTO {


    @ApiModelProperty(value = "主键id")
    private Long actId;

    @ApiModelProperty(value = "活动类型id")
    private Long actTypeId;

    @ApiModelProperty(value = "活动名称")
    private String actName;

    @ApiModelProperty(value = "活动图片地址")
    private String actImageUrl;

    @ApiModelProperty(value = "状态 0 下架 1 上架  2 已过期")
    private Integer status;

    @ApiModelProperty(value = "设备类型1 ios 2 android")
    private Integer deviceType;

    @ApiModelProperty(value = "活动详情")
    private String content;

    @ApiModelProperty(value = "是否永久活动 0否 1 是")
    private Boolean isPer;

    @ApiModelProperty(value = "活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "活动结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "备注")
    private String remark;


}
