package com.indo.admin.pojo.dto.game.sbo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public class SboAgentJsonDTO extends SboParentRequJsonDTO {

    private String Username;

    private String Password;

    private String Currency;

    private Integer Min;

    private Integer Max;

    private Integer MaxPerMatch;

    private Integer CasinoTableLimit;

    @JSONField(name="Username")
    public String getUsername() {
        return Username;
    }

    @JSONField(name="Username")
    public void setUsername(String username) {
        Username = username;
    }

    @JSONField(name="Password")
    public String getPassword() {
        return Password;
    }

    @JSONField(name="Password")
    public void setPassword(String password) {
        Password = password;
    }

    @JSONField(name="Currency")
    public String getCurrency() {
        return Currency;
    }

    @JSONField(name="Currency")
    public void setCurrency(String currency) {
        Currency = currency;
    }

    @JSONField(name="Min")
    public Integer getMin() {
        return Min;
    }

    @JSONField(name="Min")
    public void setMin(Integer min) {
        Min = min;
    }

    @JSONField(name="Max")
    public Integer getMax() {
        return Max;
    }
    @JSONField(name="Max")
    public void setMax(Integer max) {
        Max = max;
    }
    @JSONField(name="MaxPerMatch")
    public Integer getMaxPerMatch() {
        return MaxPerMatch;
    }
    @JSONField(name="MaxPerMatch")
    public void setMaxPerMatch(Integer maxPerMatch) {
        MaxPerMatch = maxPerMatch;
    }
    @JSONField(name="CasinoTableLimit")
    public Integer getCasinoTableLimit() {
        return CasinoTableLimit;
    }
    @JSONField(name="CasinoTableLimit")
    public void setCasinoTableLimit(Integer casinoTableLimit) {
        CasinoTableLimit = casinoTableLimit;
    }
}
