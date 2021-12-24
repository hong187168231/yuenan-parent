package com.indo.user.pojo.dto;


import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TestDTO extends BaseDTO {


    @NotNull(message = "{demo.key.null}")
    private Long memId;



}
