package com.live.pay.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 支付方式配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayWayConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long payWayId;

    @ApiModelProperty(value = "支付方式类型 1 支付宝 2 微信 3viettel")
    private String wayType;

    @ApiModelProperty(value = "支付方式编码 alipay 支付宝 wechatpay 微信 3viettelpay")
    private String wayTypeCode;

    @ApiModelProperty(value = "支付方式名称")
    private String wayName;

    @ApiModelProperty(value = "支付账号")
    private String wayAccount;

    @ApiModelProperty(value = "二维码")
    private String qrCode;

    @ApiModelProperty(value = "支付层id")
    private Long groupId;

    @ApiModelProperty(value = "审核状态 1 手动 2自动")
    private Long auditStatus;

    @ApiModelProperty(value = "提示")
    private String tips;

    @ApiModelProperty(value = "最小金额")
    private Integer minAmount;

    @ApiModelProperty(value = "最大金额")
    private Integer maxAmount;

    @ApiModelProperty(value = "是否删除 0 正常 1 停用")
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
