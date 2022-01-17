package com.indo.pay.pojo.bo;

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
public class PayChannel {


    @ApiModelProperty(value = "支付渠道id")
    private Long payChannelId;
    @ApiModelProperty(value = "支付渠道名称")
    private String channelName;

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
