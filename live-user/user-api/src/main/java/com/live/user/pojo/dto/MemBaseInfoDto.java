package com.live.user.pojo.dto;

import com.live.user.pojo.entity.MemBaseinfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @Author: Mr.liu
 * @Date: 2021/8/30 16:04
 * @Version: 1.0.0
 * @Desc: 请求参数实体
 */
@Data
public class MemBaseInfoDto extends MemBaseinfo {

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "分页数")
    private Integer limit;

    @ApiModelProperty(value = "注册开始时间")
    private String beginTime;

    @ApiModelProperty(value = "注册结束时间")
    private String endTime;



    @ApiModelProperty(value = "用户名，多个以/分隔")
    private String accounts;
    /**
     * 用户ID
     */
    private List<Long> ids;

    @ApiModelProperty(value = "上级代理账号")
    private String superiorAgent;

    private static final long serialVersionUID = -5214560875398830426L;
}
