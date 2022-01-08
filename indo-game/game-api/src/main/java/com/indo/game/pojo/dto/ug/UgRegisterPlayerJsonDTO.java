package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;

public class UgRegisterPlayerJsonDTO extends UgParentRequJsonDTO {

    private String MemberAccount;

    private String NickName;

    private String Currency;

    private String AgentID;

    @JSONField(name="MemberAccount")
    public String getMemberAccount() {
        return MemberAccount;
    }
    @JSONField(name="MemberAccount")
    public void setMemberAccount(String memberAccount) {
        MemberAccount = memberAccount;
    }

    @JSONField(name="NickName")
    public String getNickName() {
        return NickName;
    }
    @JSONField(name="NickName")
    public void setNickName(String nickName) {
        NickName = nickName;
    }
    @JSONField(name="Currency")
    public String getCurrency() {
        return Currency;
    }
    @JSONField(name="Currency")
    public void setCurrency(String currency) {
        Currency = currency;
    }
    @JSONField(name="AgentID")
    public String getAgentID() {
        return AgentID;
    }
    @JSONField(name="AgentID")
    public void setAgentID(String agentID) {
        AgentID = agentID;
    }
}
