package com.indo.user.pojo.req.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @des:新增站内信请求参数
 * @Author: kevin
 */
@Data
@ApiModel
public class MemNoticeAddReq {

    @ApiModelProperty("会员id")
    private Long memId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;
}
