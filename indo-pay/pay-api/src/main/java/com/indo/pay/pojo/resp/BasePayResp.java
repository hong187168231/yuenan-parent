package com.indo.pay.pojo.resp;

import java.io.Serializable;

/**
 * @ClassName BasePayResp
 * @Description 支付返回结果
 * @Author puff
 * @Date 2021/09/97 15:37
 * @Version 1.0
 **/
public class BasePayResp implements Serializable {
    private boolean flag; //返回状态
    private Integer status; //状态代码
    private String msg; //状态描述
    private String html;//返回

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public boolean isFlag() {
        return flag;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
