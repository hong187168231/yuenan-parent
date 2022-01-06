package com.indo.game.pojo.dto.sbo;

import com.alibaba.fastjson.annotation.JSONField;

public class SboPlayerLoginJsonDTO extends SboParentRequJsonDTO {

    private String Username;

    private String Portfolio;

    private Boolean IsWapSports;


    @JSONField(name="Username")
    public String getUsername() {
        return Username;
    }

    @JSONField(name="Username")
    public void setUsername(String username) {
        Username = username;
    }

    @JSONField(name="Portfolio")
    public String getPortfolio() {
        return Portfolio;
    }

    @JSONField(name="Portfolio")
    public void setPortfolio(String portfolio) {
        Portfolio = portfolio;
    }

    @JSONField(name="IsWapSports")
    public Boolean getIsWapSports() {
        return IsWapSports;
    }

    @JSONField(name="IsWapSports")
    public void setIsWapSports(Boolean isWapSports) {
        IsWapSports = isWapSports;
    }
}
