package com.live.user.pojo.vo;

import com.live.user.pojo.entity.MemLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class MemLevelVo extends MemLevel {

    @ApiModelProperty(value = "会员人数")
    private Integer count;
}
