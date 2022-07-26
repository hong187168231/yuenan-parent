package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 消息状态表
 * </p>
 *
 * @author xxx
 * @since 2022-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("msg_status_record")
@ApiModel(value="MsgStatusRecord对象", description="消息状态表")
public class MsgStatusRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "消息ID")
    private Long msgId;

    @ApiModelProperty(value = "消息类型：1系统消息")
    private Integer msgType;

    @ApiModelProperty(value = "状态：1删除，2已读")
    private Integer states;

    @ApiModelProperty(value = "会员ID")
    private Long memId;
}
