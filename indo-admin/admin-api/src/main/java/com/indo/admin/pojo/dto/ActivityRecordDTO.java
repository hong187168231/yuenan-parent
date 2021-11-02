package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ActivityRecordDTO extends BaseDTO {


    @ApiModelProperty(value = "活动类型id")
    private Long activityTypeId;

    @ApiModelProperty(value = "活动图片")
    private String activityImage;

    @ApiModelProperty(value = "状态 0 下架1 上架")
    private Integer status;

    private String activityDetail;

    @ApiModelProperty(value = "是否永久活动 0否 1 是")
    private Integer isPer;

    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "备注")
    private String remark;


}
