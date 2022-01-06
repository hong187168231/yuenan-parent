package com.indo.game.pojo.dto.sbo;

import com.alibaba.fastjson.annotation.JSONField;

public class SboRegisterPlayerJsonDTO extends SboParentRequJsonDTO {

    private String Username;

    private String Agent;

    private String UserGroup;


    @JSONField(name="Username")
    public String getUsername() {
        return Username;
    }

    @JSONField(name="Username")
    public void setUsername(String username) {
        Username = username;
    }

    @JSONField(name="Agent")
    public String getAgent() {
        return Agent;
    }

    @JSONField(name="Agent")
    public void setAgent(String agent) {
        Agent = agent;
    }

    @JSONField(name="UserGroup")
    public String getUserGroup() {
        return UserGroup;
    }

    @JSONField(name="UserGroup")
    public void setUserGroup(String userGroup) {
        UserGroup = userGroup;
    }
}
