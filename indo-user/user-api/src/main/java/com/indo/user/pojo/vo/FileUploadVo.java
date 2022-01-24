package com.indo.user.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "文件上传vo")
@AllArgsConstructor
public class FileUploadVo {

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

}
