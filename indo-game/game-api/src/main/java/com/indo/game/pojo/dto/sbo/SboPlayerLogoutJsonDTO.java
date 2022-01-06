package com.indo.game.pojo.dto.sbo;

import com.alibaba.fastjson.annotation.JSONField;

public class SboPlayerLogoutJsonDTO extends SboParentRequJsonDTO {

    private String Username;

    @JSONField(name="Username")
    public String getUsername() {
        return Username;
    }

    @JSONField(name="Username")
    public void setUsername(String username) {
        Username = username;
    }
}
