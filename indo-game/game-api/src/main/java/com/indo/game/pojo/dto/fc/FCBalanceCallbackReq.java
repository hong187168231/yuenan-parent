package com.indo.game.pojo.dto.fc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class FCBalanceCallbackReq {
    // 玩家账号
    @JSONField(name = "MemberAccount")
    private String memberAccount;
    // 币种
    @JSONField(name = "Currency")
    private String currency;
    // 游戏ID
    @JSONField(name = "GameID")
    private Integer gameId;
}
