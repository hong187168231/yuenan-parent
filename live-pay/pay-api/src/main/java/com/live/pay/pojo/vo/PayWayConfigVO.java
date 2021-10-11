package com.live.pay.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付方式配置返回
 */
@Data
public class PayWayConfigVO extends BaseEntity {
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

    @ApiModelProperty(value = "支付层级")
    private String groupName;

    @ApiModelProperty(value = "审核状态 1 手动 2自动")
    private Long auditStatus;

    @ApiModelProperty(value = "是否删除 0 正常 1 停用")
    private Boolean isDel;

    @ApiModelProperty(value = "备注")
    private String remark;

}
