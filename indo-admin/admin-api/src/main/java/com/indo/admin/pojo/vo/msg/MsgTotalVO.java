package com.indo.admin.pojo.vo.msg;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class MsgTotalVO implements Serializable {


    @ApiModelProperty(value = "系统消息数量")
    private Integer sysMsgTotal;

    @ApiModelProperty(value = "个人消息数量")
    private Integer personalMsgTotal;


}
