package com.indo.game.pojo.entity.sbo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_sbo_oper_info")
public class SboOperInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员名称。长度不得大于20字元,仅允许数字或字母且会员名称需唯一")
    @NotNull(message = "会员名称不能为空")
    private String userName;

    @ApiModelProperty(value = "产品类别")
    @NotNull(message = "产品类别不能为空")
    private String productType;

    @ApiModelProperty(value = "游戏类别")
    @NotNull(message = "游戏类别不能为空")
    private String gameType;

    @ApiModelProperty(value = "每当会员下注时，它会根据该笔注单生成一个唯一的转移代码")
    @NotNull(message = "唯一的转移代码(transferCode)不能为空")
    private String transferCode;

    @ApiModelProperty(value = "当产品类别为1,3或7时，交易代号将会与转移代码相同。当产品类别为9时，将会使用此参数来传送在各个第三方游戏底下的局号。")
    private String transactionId;

    @ApiModelProperty(value = "输赢多少金额。其中包括会员的投注金额。")
    private BigDecimal winloss;//输赢多少金额。其中包括会员的投注金额。

    @ApiModelProperty(value = "赌注的结果 : {赢:0,输:1,平手:2}")
    private String resultType;//赌注的结果 : {赢:0,输:1,平手:2}

    @ApiModelProperty(value = "操作时间")
    private String resultTime;

    @ApiModelProperty(value = "下注金额")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "存款余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "操作状态")
    private String status;

    @ApiModelProperty(value = "操作类别")
    @NotNull(message = "操作类别不能为空")
    private String operType;

    @ApiModelProperty(value = "转帐交易")
    private String transferType;

   }
