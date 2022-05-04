package com.indo.game.pojo.dto.sa;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SA登录请求返回对象
 */
@XmlRootElement(name = "LoginRequestResponse")
public class SaLoginResp {

    // 登录令牌
    private String token;

    // 系统内使用的登入名称
    private String displayName;

    // 0正常
    private Integer errorMsgId;

    private String errorMsg;

    public String getToken() {
        return token;
    }

    @XmlElement(name = "Token")
    public void setToken(String token) {
        this.token = token;
    }

    public String getDisplayName() {
        return displayName;
    }

    @XmlElement(name = "DisplayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getErrorMsgId() {
        return errorMsgId;
    }

    @XmlElement(name = "ErrorMsgId")
    public void setErrorMsgId(Integer errorMsgId) {
        this.errorMsgId = errorMsgId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @XmlElement(name = "ErrorMsg")
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
