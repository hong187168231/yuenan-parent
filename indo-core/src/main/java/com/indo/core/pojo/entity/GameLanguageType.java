package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author xxx
 * @since 2021-11-22
 */
@Data
@TableName("game_language_type")
@ApiModel(value="LanguageType对象", description="")
public class GameLanguageType{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "语言标准代码")
    private String code;

    @ApiModelProperty(value = "英文名称")
    private String enName;

    @ApiModelProperty(value = "中文名称")
    private String cnName;


}
