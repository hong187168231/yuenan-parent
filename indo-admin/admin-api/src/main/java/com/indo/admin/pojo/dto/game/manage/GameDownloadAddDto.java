package com.indo.admin.pojo.dto.game.manage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameDownloadAddDto {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "下载地址")
    @NotNull(message = "下载地址不能为空")
    private String download;

    @ApiModelProperty(value = "是否启用")
    private String isStart;//0关闭  1启用
}
