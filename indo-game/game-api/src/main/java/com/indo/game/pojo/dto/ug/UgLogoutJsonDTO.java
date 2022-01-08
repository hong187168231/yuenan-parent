package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class UgLogoutJsonDTO extends UgParentRequJsonDTO {

    private String MemberAccount;// string 是 账户名

    @JSONField(name="MemberAccount")
    public String getMemberAccount() {
        return MemberAccount;
    }
    @JSONField(name="MemberAccount")
    public void setMemberAccount(String memberAccount) {
        MemberAccount = memberAccount;
    }
}
