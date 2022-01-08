package com.indo.game.pojo.dto.ug;


import com.alibaba.fastjson.annotation.JSONField;
import com.indo.common.config.OpenAPIProperties;

public class UgParentRequJsonDTO {

    private String CompanyKey = OpenAPIProperties.UG_KEY;

    private String APIPassword = OpenAPIProperties.UG_API_PASSWORD;

    @JSONField(name="CompanyKey")
    public String getCompanyKey() {
        return CompanyKey;
    }

    @JSONField(name="CompanyKey")
    public void setCompanyKey(String companyKey) {
        CompanyKey = companyKey;
    }

    @JSONField(name="APIPassword")
    public String getAPIPassword() {
        return APIPassword;
    }

    @JSONField(name="APIPassword")
    public void setAPIPassword(String APIPassword) {
        this.APIPassword = APIPassword;
    }
}
