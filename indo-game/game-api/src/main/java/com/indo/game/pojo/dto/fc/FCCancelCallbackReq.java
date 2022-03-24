package com.indo.game.pojo.dto.fc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class FCCancelCallbackReq {
    // 玩家账号
    @JSONField(name = "MemberAccount")
    private String memberAccount;
    // 交易序号
    @JSONField(name = "BankID")
    private String bankID;
}
