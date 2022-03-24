package com.indo.game.pojo.dto.jili;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JiliCallbackAuthResp extends JiliApiResponse {
    // 账号
    private String username;
    private String currency;
    private BigDecimal balance;

    // 非必填 不列入 md5 加密，游戏回主页功能导向位置
    private String token;
    // 下注返回序号
    private String txId;
}
