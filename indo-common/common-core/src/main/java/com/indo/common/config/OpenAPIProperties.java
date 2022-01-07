package com.indo.common.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIProperties{

    /**
     * 代理地址
     */
    private String proxyHostName;
    private int proxyPort;
    private String proxyTcp;

    private String awcCert;
    private String awcAgentId;
    private String awcApiId;
    private String awcApiSecretKey;

    private String companyKey;
    private String sboApiUrl;
    private String sboServerId;

    private String siteName;
    private String vendorId;
    private String sabaApiUrl;

    private String ugCompanyKey;
    private String ugApiPasword;
    private String ugAgentID;
    private String ugApiUrl;

    public String getProxyHostName() {
        return proxyHostName;
    }

    @Value("${proxy.hostName}")
    public void setProxyHostName(String proxyHostName) {
        this.proxyHostName = proxyHostName;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    @Value("${proxy.port}")
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyTcp() {
        return proxyTcp;
    }

    @Value("${proxy.tcp}")
    public void setProxyTcp(String proxyTcp) {
        this.proxyTcp = proxyTcp;
    }

    public String getAwcCert() {
        return awcCert;
    }

    @Value("${awc.cert}")
    public void setAwcCert(String awcCert) {
        this.awcCert = awcCert;
    }

    public String getAwcAgentId() {
        return awcAgentId;
    }

    @Value("${awc.agentId}")
    public void setAwcAgentId(String awcAgentId) {
        this.awcAgentId = awcAgentId;
    }

    public String getAwcApiId() {
        return awcApiId;
    }

    @Value("${awc.awcApiId}")
    public void setAwcApiId(String awcApiId) {
        this.awcApiId = awcApiId;
    }

    public String getAwcApiSecretKey() {
        return awcApiSecretKey;
    }

    @Value("${awc.awcApiSecretKey}")
    public void setAwcApiSecretKey(String awcApiSecretKey) {
        this.awcApiSecretKey = awcApiSecretKey;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    @Value("${sbo.companyKey}")
    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getSboApiUrl() {
        return sboApiUrl;
    }

    @Value("${sbo.sboApiUrl}")
    public void setSboApiUrl(String sboApiUrl) {
        this.sboApiUrl = sboApiUrl;
    }

    public String getSboServerId() {
        return sboServerId;
    }

    @Value("${sbo.sboServerId}")
    public void setSboServerId(String sboServerId) {
        this.sboServerId = sboServerId;
    }

    public String getSiteName() {
        return siteName;
    }

    @Value("${saba.SiteName}")
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getVendorId() {
        return vendorId;
    }

    @Value("${saba.vendor_id}")
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getSabaApiUrl() {
        return sabaApiUrl;
    }

    @Value("${saba.sabaApiUrl}")
    public void setSabaApiUrl(String sabaApiUrl) {
        this.sabaApiUrl = sabaApiUrl;
    }

    public String getUgCompanyKey() {
        return ugCompanyKey;
    }

    @Value("${ug.CompanyKey}")
    public void setUgCompanyKey(String ugCompanyKey) {
        this.ugCompanyKey = ugCompanyKey;
    }

    public String getUgApiPasword() {
        return ugApiPasword;
    }

    @Value("${ug.APIPassword}")
    public void setUgApiPasword(String ugApiPasword) {
        this.ugApiPasword = ugApiPasword;
    }

    public String getUgAgentID() {
        return ugAgentID;
    }

    @Value("${ug.AgentID}")
    public void setUgAgentID(String ugAgentID) {
        this.ugAgentID = ugAgentID;
    }

    public String getUgApiUrl() {
        return ugApiUrl;
    }

    @Value("${ug.ugApiUrl}")
    public void setUgApiUrl(String ugApiUrl) {
        this.ugApiUrl = ugApiUrl;
    }
}