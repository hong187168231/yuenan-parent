package com.indo.admin.modules.mem.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.indo.common.base.BaseDTO;
import com.indo.common.enums.BankCategoryEnum;
import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel
public class MemBankRelationPageReq extends BaseDTO {

    @ApiModelProperty("银行类别枚举 MEM_ID (会员id) | BANK_CAED_NO (银行卡号) | CITY (城市) | BANK_NAME (银行名称)")
    private BankCategoryEnum bankCategoryEnum;

    @ApiModelProperty("搜索关键字")
    private String keyword;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endTime;


}
