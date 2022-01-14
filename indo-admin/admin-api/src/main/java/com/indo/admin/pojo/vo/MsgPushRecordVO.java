package com.indo.admin.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MsgPushRecordVO {


    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "推送终端: 0 全部 1 ios  2 android")
    private Integer deviceType;

    @ApiModelProperty(value = "是否删除 0 未删除 1 删除")
    private Boolean isDel;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
