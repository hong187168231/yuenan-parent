package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 游戏种类字典表
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_type")
@ApiModel(value="GameType对象", description="游戏种类字典表")
public class GameType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "游戏编号")
    private String gameCode;

    @ApiModelProperty(value = "游戏英文名称")
    private String gameEnName;

    @ApiModelProperty(value = "游戏中文名称")
    private String gameCnName;

    @ApiModelProperty(value = "游戏平台id")
    private Integer platformId;

    @ApiModelProperty(value = "游戏分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "是否启用")
    private String isStart;//0关闭  1启用

}
