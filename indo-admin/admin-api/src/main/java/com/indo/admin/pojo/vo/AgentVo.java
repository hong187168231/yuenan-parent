package com.indo.admin.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Mr.liu
 * @Date: 2021/9/8 9:17
 * @Version: 1.0.0
 * @Desc: 代理对象
 */
@Data
public class AgentVo implements Serializable {

    @ApiModelProperty(value = "代理ID")
    private Long id;

    @ApiModelProperty(value = "代理账户")
    private String account;

    @ApiModelProperty(value = "上级代理账号")
    private String superiorAgent;

    @ApiModelProperty(value = "账户类型")
    private Integer accountType;

    @ApiModelProperty(value = "下级数")
    private Integer subordinateNum;

    @ApiModelProperty(value = "团队数")
    private Integer teamNum;

    @ApiModelProperty(value = "注册时间")
    private Date createTime;



    private static final long serialVersionUID = -5649061999296161199L;
}
