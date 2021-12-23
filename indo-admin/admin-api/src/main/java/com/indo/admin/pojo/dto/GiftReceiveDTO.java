package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import com.indo.common.enums.GiftTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "GiftReceiveDTO对象", description = "礼金领取记录查询请求体")
@Data
public class GiftReceiveDTO extends BaseDTO {

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "礼金类型")
    private GiftTypeEnum giftTypeEnum;

    @ApiModelProperty(hidden = true)
    private Integer giftType;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;


    public Integer getGiftType() {
        if (giftTypeEnum == null) {
            return null;
        }
        return giftTypeEnum.getCode();
    }
}
