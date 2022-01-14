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
 * 
 * </p>
 *
 * @author xxx
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AgentCashApply对象", description="")
public class AgentCashApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "会员等级")
    private Integer memLevel;

    @ApiModelProperty(value = "真实姓名")
    private String amount;

    private String branchBank;

    private String bankName;

    private Integer bankCardNo;

    private String city;

    private String ifsc;

    private String orderNo;

    private String channelName;

    private String cashStatus;

    @ApiModelProperty(value = "发放人")
    private String createUser;


}
