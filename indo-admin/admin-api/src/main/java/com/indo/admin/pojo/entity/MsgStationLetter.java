package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 站内信
 * </p>
 *
 * @author puff
 * @since 2021-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgStationLetter extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 发送人id
     */
    private Long sendMemId;

    /**
     * 接收人
     */
    private String receiver;

    private Integer receiverLevel;

    private Integer receivePayGroup;

    /**
     * 发送类型: 1 按收件人发送 2 按会员等级发送 3 按支付层级发送
     */
    private Integer sendType;

    /**
     * 是否删除 0 未删除 1 删除
     */
    private Boolean isDel;

    /**
     * 备注
     */
    private String remark;



}
