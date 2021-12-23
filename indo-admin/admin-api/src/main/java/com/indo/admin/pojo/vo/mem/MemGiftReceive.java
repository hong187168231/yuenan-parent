package com.indo.admin.pojo.vo.mem;

import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="MemGiftReceive对象", description="活动类型表")
public class MemGiftReceive extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long receiveId;

    private Long memId;

    @ApiModelProperty(value = "礼金类型")
    private String giftType;

    @ApiModelProperty(value = "礼金编码")
    private String giftCode;


    @ApiModelProperty(value = "礼金金额")
    private Integer giftAmount;

    @ApiModelProperty(value = "礼金名称")
    private Integer giftName;

    @ApiModelProperty(value = "备注")
    private String remark;


}
