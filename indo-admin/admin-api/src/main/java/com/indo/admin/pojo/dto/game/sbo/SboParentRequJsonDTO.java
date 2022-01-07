package com.indo.admin.pojo.dto.game.sbo;


import com.alibaba.fastjson.annotation.JSONField;
import com.indo.common.config.OpenAPIProperties;

public class SboParentRequJsonDTO {

    private String CompanyKey = OpenAPIProperties.SBO_KEY;

    private String ServerId = OpenAPIProperties.SBO_SERVERID;

    @JSONField(name="CompanyKey")
    public String getCompanyKey() {
        return CompanyKey;
    }

    @JSONField(name="CompanyKey")
    public void setCompanyKey(String companyKey) {
        CompanyKey = companyKey;
    }

    @JSONField(name="ServerId")
    public String getServerId() {
        return ServerId;
    }

    @JSONField(name="ServerId")
    public void setServerId(String serverId) {
        ServerId = serverId;
    }
}
