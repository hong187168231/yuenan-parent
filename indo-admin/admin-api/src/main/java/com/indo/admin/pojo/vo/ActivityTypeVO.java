package com.indo.admin.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityTypeVO {

    @ApiModelProperty(value = "主键")
    private Long actTypeId;

    @ApiModelProperty(value = "活动类型名称")
    private String actTypeName;

    @ApiModelProperty(value = "活动数量")
    private Integer actNum;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
