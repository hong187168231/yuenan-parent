package com.indo.game.pojo.entity.ug;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
@Data
@TableName("game_ug_insbet")
public class InsBet {
    private long insId;// long 是 保险单编号
    private BigDecimal betValue;// decimal 是 出售金额
    private BigDecimal backValue;// decimal 是 返还金额
    private String betDate;// datetime 是 出售日期
    private BigDecimal Win;// decimal 是 输赢金额
}
