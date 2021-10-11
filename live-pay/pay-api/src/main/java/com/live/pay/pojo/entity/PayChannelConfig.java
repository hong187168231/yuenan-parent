package com.live.pay.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 支付渠道置实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayChannelConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long payChannelId;

    @ApiModelProperty(value = "支付渠道名称")
    private String channelName;

    @ApiModelProperty(value = "支付渠道描述")
    private String channelDesc;

    @ApiModelProperty(value = "支付渠道类型")
    private String channeType;

    @ApiModelProperty(value = "支付地址")
    private String payUrl;

    @ApiModelProperty(value = "是否正常 0 正常 1 停用")
    private Boolean isDel;

    @ApiModelProperty(value = "排序字段")
    private Integer sortBy;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "最后更新人")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
