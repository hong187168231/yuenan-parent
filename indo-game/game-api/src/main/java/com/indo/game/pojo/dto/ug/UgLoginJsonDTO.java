package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class UgLoginJsonDTO extends UgParentRequJsonDTO {

    private String MemberAccount;// string 是 账户名
    private String WebType;// string 否 登录类型: PC \Smart \Wap；默认值：PC
    private String LoginIP;// string 是 登录 IP
    private String Language;// string 否 语言文字代码；默认值：EN
    private String PageStyle;// string 否 网站版面 SP1, SP2, SP3, SP4, SP5,SP6,SP7；默认值：SP1
    private String OddsStyle;// string 否 赔率样式代码(OddsStyle) ；默认值：MY
    private String HostUrl;// string 否 对接商域名,如果有 cname 指向我们域名的可以传入该参 数，指向的域名询问我们客服
    private String PUrl;// string 否 对接商的首页的链接，仅 Smart 版有效
    private BigDecimal Balance;// decimal 否 用于登录后的余额展示，仅 H5 版有效
    private BigDecimal CashBalance;// decimal 否 用于登录后的现金余额展示，仅 H5 版有效

    @JSONField(name="MemberAccount")
    public String getMemberAccount() {
        return MemberAccount;
    }
    @JSONField(name="MemberAccount")
    public void setMemberAccount(String memberAccount) {
        MemberAccount = memberAccount;
    }
    @JSONField(name="WebType")
    public String getWebType() {
        return WebType;
    }
    @JSONField(name="WebType")
    public void setWebType(String webType) {
        WebType = webType;
    }
    @JSONField(name="LoginIP")
    public String getLoginIP() {
        return LoginIP;
    }
    @JSONField(name="LoginIP")
    public void setLoginIP(String loginIP) {
        LoginIP = loginIP;
    }
    @JSONField(name="Language")
    public String getLanguage() {
        return Language;
    }
    @JSONField(name="Language")
    public void setLanguage(String language) {
        Language = language;
    }
    @JSONField(name="PageStyle")
    public String getPageStyle() {
        return PageStyle;
    }
    @JSONField(name="PageStyle")
    public void setPageStyle(String pageStyle) {
        PageStyle = pageStyle;
    }
    @JSONField(name="OddsStyle")
    public String getOddsStyle() {
        return OddsStyle;
    }
    @JSONField(name="OddsStyle")
    public void setOddsStyle(String oddsStyle) {
        OddsStyle = oddsStyle;
    }
    @JSONField(name="HostUrl")
    public String getHostUrl() {
        return HostUrl;
    }
    @JSONField(name="HostUrl")
    public void setHostUrl(String hostUrl) {
        HostUrl = hostUrl;
    }
    @JSONField(name="PUrl")
    public String getPUrl() {
        return PUrl;
    }
    @JSONField(name="PUrl")
    public void setPUrl(String PUrl) {
        this.PUrl = PUrl;
    }
    @JSONField(name="Balance")
    public BigDecimal getBalance() {
        return Balance;
    }
    @JSONField(name="Balance")
    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }
    @JSONField(name="CashBalance")
    public BigDecimal getCashBalance() {
        return CashBalance;
    }
    @JSONField(name="CashBalance")
    public void setCashBalance(BigDecimal cashBalance) {
        CashBalance = cashBalance;
    }
}
