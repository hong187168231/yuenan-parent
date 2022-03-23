package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import com.indo.common.enums.ChangeCategoryEnum;
import com.indo.common.enums.GiftTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;


@ApiModel(value = "CapitalDTO对象", description = "资金明细查询请求体")
@Data
public class CapitalDTO extends BaseDTO {

    @ApiModelProperty(value = "交易类别")
    private ChangeCategoryEnum changeCategory;

    @ApiModelProperty(value = "关联订单号")
    private String refNo;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(hidden = true)
    private Set<Integer> changeTypes;

    @ApiModelProperty(value = "会员账号")
    private Long account;

}
