package com.indo.user.pojo.req.mem;

import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询个人银行卡列表请求参数类")
public class BankCardPageReq extends QueryParam {

    @ApiModelProperty(value = "用户id", required = true)
    private String memId;
}
