package com.indo.game.pojo.dto.sbo;

import com.alibaba.fastjson.annotation.JSONField;

public class SboCallBackParentReq {
    private String CompanyKey;
    private String Username;
    private int ProductType;
    private int GameType;
    private int Gpid;
    @JSONField(name="CompanyKey")
    public String getCompanyKey() {
        return CompanyKey;
    }
    @JSONField(name="CompanyKey")
    public void setCompanyKey(String companyKey) {
        CompanyKey = companyKey;
    }
    @JSONField(name="Username")
    public String getUserName() {
        return Username;
    }
    @JSONField(name="Username")
    public void setUserName(String userName) {
        Username = userName;
    }
    @JSONField(name="ProductType")
    public int getProductType() {
        return ProductType;
    }
    @JSONField(name="ProductType")
    public void setProductType(int productType) {
        ProductType = productType;
    }
    @JSONField(name="GameType")
    public int getGameType() {
        return GameType;
    }
    @JSONField(name="GameType")
    public void setGameType(int gameType) {
        GameType = gameType;
    }
    @JSONField(name="Gpid")
    public int getGpid() {
        return Gpid;
    }
    @JSONField(name="Gpid")
    public void setGpid(int gpid) {
        Gpid = gpid;
    }
}
