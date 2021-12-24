package com.indo.common.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页")
    private Integer page = 1;

    @ApiModelProperty(value = "分页数")
    private Integer limit = 10;

}
