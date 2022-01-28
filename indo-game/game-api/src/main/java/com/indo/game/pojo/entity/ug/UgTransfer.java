package com.indo.game.pojo.entity.ug;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("game_ug_transfer")
public class UgTransfer {

    private long id;
    private boolean bet;//  Boolean 是 此交易是否是投注
    private String account;//  String 是 登录帐号
    private BigDecimal amount;//  Decimal 是 金额
    private String transactionNo;//  String 是 交易号
    private String betId;//  String 是 注单编号
    private String status;//Transfer,Cancel
}
