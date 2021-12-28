package com.indo.user.pojo.vo.act;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
@ApiModel(value = "Activity对象", description = "活动")
public class ActivityVo {


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

    @ApiModelProperty(value = "活动详情")
    private String content;

    @ApiModelProperty(value = "是否永久活动 0否 1 是")
    private Boolean isPer;

    @ApiModelProperty(value = "活动开始时间")
    private String beginTime;

    @ApiModelProperty(value = "活动结束时间")
    private String endTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "备注")
    private String remark;


}
