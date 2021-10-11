package com.live.user.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MembankRelationDTO {
    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "分页数")
    private Integer limit;

    @ApiModelProperty(value = "所属类别: 0 用户名；1 姓名；2 银行卡号, 不传就是全部")
    private Integer type;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "查询账号类型: 0 普通会员 1 代理会员 2 带玩会员, 不传就是全部")
    private Integer memType;

    @ApiModelProperty(value = "注册开始时间")
    private String beginTime;

    @ApiModelProperty(value = "注册结束时间")
    private String endTime;

    @ApiModelProperty(value = "用户银行卡信息主键，导出时，这个为必传")
    private List<Long> ids;
}
