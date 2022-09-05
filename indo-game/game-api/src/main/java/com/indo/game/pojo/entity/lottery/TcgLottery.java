package com.indo.game.pojo.entity.lottery;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@TableName("game_tcg_lottery")
public class TcgLottery {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "产品类型")
    private int game_prod_type;
    @ApiModelProperty(value = "彩种组游戏代码")
    private String game_group_code;
    @ApiModelProperty(value = "奖金模式 : 1 為傳統模式")
    private String prize_mode_id;
    @ApiModelProperty(value = "彩种組最低奖金系列")
    private String min_series;
    @ApiModelProperty(value = "彩种組最高奖金系列")
    private String max_series;
    @ApiModelProperty(value = "该彩种組最大可投注系列，低于或等于max_series值")
    private String max_bet_series;
    @ApiModelProperty(value = "该彩种組预设投注系列")
    private String default_series;
}
