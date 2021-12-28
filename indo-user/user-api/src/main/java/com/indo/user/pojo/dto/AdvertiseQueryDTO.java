package com.indo.user.pojo.dto;

import com.indo.common.base.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AdvertiseQueryDTO extends BaseDTO {


//    @NotNull(message = "标题不能为空")
    private String title;


}
