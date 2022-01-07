package com.indo.admin.pojo.dto.game.sbo;


import com.alibaba.fastjson.annotation.JSONField;
import com.indo.common.config.OpenAPIProperties;

public class SboParentRequJsonDTO {

    OpenAPIProperties openAPIProperties = new OpenAPIProperties();
    private String CompanyKey = openAPIProperties.getCompanyKey();

    private String ServerId = openAPIProperties.getSboServerId();

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
