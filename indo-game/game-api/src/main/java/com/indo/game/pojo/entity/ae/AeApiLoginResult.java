package com.indo.game.pojo.entity.ae;

/**
 * 登录成功返回结果对象
 */
public class AeApiLoginResult {

    private String orderid;
    private String url;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "AeApiLoginResult{" +
                "orderid='" + orderid + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
