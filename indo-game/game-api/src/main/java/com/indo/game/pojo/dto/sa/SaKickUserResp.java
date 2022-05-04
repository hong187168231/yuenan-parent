package com.indo.game.pojo.dto.sa;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SA退出请求返回对象
 */
@XmlRootElement(name = "KickUserResponse")
public class SaKickUserResp {

    // 0正常
    private Integer errorMsgId;

    private String errorMsg;

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
