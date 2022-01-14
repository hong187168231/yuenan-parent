package com.indo.admin.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MsgStationLetterVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "接收人 (会员账号)")
    private String receiver;

    @ApiModelProperty(value = "接收等级")
    private Integer receiverLevel;

    @ApiModelProperty(value = "接收支付组")
    private Integer receivePayGroup;

    @ApiModelProperty(value = "发送类型: 1 按收件人发送 2 按会员等级发送 3 按支付层级发送")
    private Integer sendType;

    @ApiModelProperty(value = "是否删除 0 未删除 1 删除")
    private Boolean isDel;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "备注")
    private String remark;
}
