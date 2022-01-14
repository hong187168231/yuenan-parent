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
 * 
 * </p>
 *
 * @author xxx
 * @since 2022-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_agent")
@ApiModel(value="GameAgent对象", description="")
public class GameAgent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "唯一的代理名称")
    private String username;

    @ApiModelProperty(value = "代理用以登入后台的密码")
    private String password;

    @ApiModelProperty(value = "币别")
    private String currency;

    @ApiModelProperty(value = "该代理底下玩家的预设单笔注单最低限额")
    private Integer min;

    @ApiModelProperty(value = "该代理底下玩家的预设单笔注单最高限额")
    private Integer max;

    @ApiModelProperty(value = "该代理底下玩家的预设单场比赛最高限额")
    private Integer maxPerMatch;

    @ApiModelProperty(value = "该代理底下玩家的预设真人赌场限额设定。 1： 低 2：中 3：高 4：VIP")
    private Integer casinoTableLimit;

    @ApiModelProperty(value = "平台名称")
    private String parentName;

    @ApiModelProperty(value = "代理状态")
    private String status;


}
