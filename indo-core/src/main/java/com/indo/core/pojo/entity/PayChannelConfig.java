package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 支付渠道配置
 * </p>
 *
 * @author xxx
 * @since 2021-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PayChannelConfig对象", description = "支付渠道配置")
public class PayChannelConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "支付渠道id")
    @TableId(value = "pay_channel_id", type = IdType.AUTO)
    private Long payChannelId;

    @ApiModelProperty(value = "支付渠道名称")
    private String channelName;

    @ApiModelProperty(value = "别名")
    private String alias;

    private String channelCode;

    @ApiModelProperty(value = "支付渠道描述")
    private String channelDesc;

    private String merchantNo;

    private String payUrl;

    private String notifyUrl;

    private String pageUrl;

    private String secretKey;

    @ApiModelProperty(value = "支付渠道类型 1 第三方 2 银联 3 人工")
    private Integer channelType;

    private Integer sortBy;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "备注")
    private String remark;

    private Integer status;


}
