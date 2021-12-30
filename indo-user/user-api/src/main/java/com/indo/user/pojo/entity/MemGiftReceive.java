package com.indo.user.pojo.entity;

import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 活动类型表
 * </p>
 *
 * @author xxx
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MemGiftReceive对象", description = "活动类型表")
public class MemGiftReceive extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long receiveId;

    private Long memId;

    @ApiModelProperty(value = "礼金类型")
    private Integer giftType;

    @ApiModelProperty(value = "礼金编码")
    private String giftCode;

    @ApiModelProperty(value = "礼金名称")
    private String giftName;

    @ApiModelProperty(value = "晋升等级")
    private Integer upLevel;

    @ApiModelProperty(value = "备注")
    private String remark;


}
