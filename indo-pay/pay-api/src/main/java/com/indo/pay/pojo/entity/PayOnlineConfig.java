package com.indo.pay.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 在线支付配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayOnlineConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long payOnlineId;

    @ApiModelProperty(value = "支付渠道ID")
    private Long payChannelId;

    @ApiModelProperty(value = "支付名称")
    private String payName;

    @ApiModelProperty(value = "商户号")
    private String merchantNo;

    @ApiModelProperty(value = "商户密钥")
    private String merchantSecret;

    @ApiModelProperty(value = "第三方公钥")
    private String thirdPublicSecret;

    @ApiModelProperty(value = "第三方域名")
    private String thirdUrl;

    @ApiModelProperty(value = "终端号")
    private String terminalNo;

    @ApiModelProperty(value = "支付请求网关地址")
    private String payGateway;

    @ApiModelProperty(value = "返回地址")
    private String callbackUrl;

    @ApiModelProperty(value = "最小金额")
    private Integer minAmount;

    @ApiModelProperty(value = "最大金额")
    private Integer maxAmount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否删除 0 正常 1 停用")
    private Boolean isDel;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "最后更新人")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

}
