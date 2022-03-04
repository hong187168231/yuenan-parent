package com.indo.game.pojo.vo.callback.jdb;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class JdbApiIsGameingInfoRequestBack {
    private Integer gType;// Integer 游戏型态
    private Integer mType;// Integer 机台类型
    private String loginFrom;// String(20) 玩家从网页或行动装置登入
    private String ipAddr;// String(50) 玩家登入 IP
    private String loginTime;// String(19) 登入游戏时间 (dd-MM-yyyy HH:mm:ss)
    private BigDecimal balance;// Number 账户余额
}
