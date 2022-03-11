package com.indo.admin.pojo.vo.agent;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgentSpreadVO {
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "跳转地址")
    private String skipUrl;

    @ApiModelProperty(value = "内容详情")
    private String content;

    @ApiModelProperty(value = "状态 0 下架1 上架")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
