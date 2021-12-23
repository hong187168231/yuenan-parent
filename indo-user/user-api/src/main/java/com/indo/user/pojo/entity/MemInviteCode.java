package com.indo.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会员邀请码
 * </p>
 *
 * @author xxx
 * @since 2021-12-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MemInviteCode对象", description="会员邀请码")
public class MemInviteCode extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long memId;

    private String account;

    @ApiModelProperty(value = "会员邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "状态：1启用 2-禁用")
    private Boolean status;


}
