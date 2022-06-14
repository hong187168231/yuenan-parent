package com.indo.game.pojo.dto.sa;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * SA登录请求返回对象
 */
@XmlRootElement(name = "RequestResponse")
public class SaCallbackResp {

    // 用户名
    private String username;

    // 币种
    private String currency;

    // 余额
    private BigDecimal amount;

    // 0正常
    private Integer error;
    @XmlElement(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    @XmlElement(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    @XmlElement(name = "error")
    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }
}
