package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * app版本更新表
 * </p>
 *
 * @author xxx
 * @since 2022-01-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AppVersion对象", description="app版本更新表")
public class AppVersion extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "版本id")
    @TableId(value = "version_id", type = IdType.AUTO)
    private Integer versionId;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "版本名称")
    private String versionName;

    @ApiModelProperty(value = "更新类型, 0: 非弹框，1: 弹框，2: 强制")
    private Integer updateType;

    private Integer deviceType;

    @ApiModelProperty(value = "下载地址")
    private String downloadUrl;

    @ApiModelProperty(value = "更新内容")
    private String updateContent;

    @ApiModelProperty(value = "状态 启用 - 禁用")
    private Integer enable;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime releaseTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;


}
