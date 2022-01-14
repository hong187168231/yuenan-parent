package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 短信发送记录表
 * </p>
 *
 * @author xxx
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sms_send_record")
@ApiModel(value="SendRecord对象", description="短信发送记录表")
public class SmsSendRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "短信发送记录id")
    private Long smsSendId;

    @ApiModelProperty(value = "区号")
    private String areaCode;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "短信类型")
    private Integer smsType;

    @ApiModelProperty(value = "短信内容")
    private String content;

    @ApiModelProperty(value = "短信编码")
    private String smsCode;

    @ApiModelProperty(value = "状态 0失败 1发送成功 1已使用")
    private Integer status;

    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    @ApiModelProperty(value = "创建人")
    private String createUser;


}
