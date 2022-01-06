package com.indo.admin.pojo.dto.game.sbo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public class SboUpdateAgentStatusJsonDTO  extends SboParentRequJsonDTO{

    private String Username;

    private String Status;
    @JSONField(name="Username")
    public String getUsername() {
        return Username;
    }
    @JSONField(name="Username")
    public void setUsername(String username) {
        Username = username;
    }
    @JSONField(name="Status")
    public String getStatus() {
        return Status;
    }
    @JSONField(name="Status")
    public void setStatus(String status) {
        Status = status;
    }
}
