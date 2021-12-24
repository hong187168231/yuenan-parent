package com.indo.admin.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ActivityTypeDTO extends BaseDTO {

    @ApiModelProperty(value = "主键id")
    private Long actTypeId;

    @ApiModelProperty(value = "活动类型名称")
    private String actTypeName;

    @ApiModelProperty(value = "活动数量")
    private Integer actNum;


}
