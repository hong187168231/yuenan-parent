package com.indo.user.pojo.req;

import com.indo.common.base.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AdvertiseQueryReq extends BaseDTO {


//    @NotNull(message = "标题不能为空")
    private String title;


}
