package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;

public class UgTasksJsonBetIdDTO extends UgParentRequJsonDTO {

    private String BetID;// 注单 ID

    @JSONField(name="BetID")
    public String getBetID() {
        return BetID;
    }

    @JSONField(name="BetID")
    public void setBetID(String betID) {
        BetID = betID;
    }
}
