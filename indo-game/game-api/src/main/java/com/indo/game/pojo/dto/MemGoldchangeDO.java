package com.indo.game.pojo.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.indo.game.pojo.entity.MemGoldchange;

import java.math.BigDecimal;
import java.util.Date;

public class MemGoldchangeDO extends MemGoldchange {

//    原直播
//    private Integer changetype;
//
//    private BigDecimal quantity;
//
//    private Integer type;
//
//    private String opnote;
//
//    private String prename;
    /**
     * 会员账号
     */
    private String account;
    /**
     * 操作类型
     */
    private Integer type;

    private String prename;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creatTime;

    /**
     * 说明: 累计投注（元）变动值
     */
    private BigDecimal betAmount;

    /**
     * 说明: 待开奖金额（元）变动值
     */
    private BigDecimal waitAmount;

    /**
     * 说明: 累计充值（元）变动值
     */
    private BigDecimal payAmount;

    /**
     * 说明: 累计提现（元）变动值
     */
    private BigDecimal withdrawalAmount;

    /**
     * 说明: 不可提现金额（元）变动值
     */
    private BigDecimal noWithdrawalAmount;

    /**
     * 记录账变记录的值
     */
    private BigDecimal showChange;

    /**
     * cpt用户用户uid
     *
     * @return
     */
    private Integer userId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public BigDecimal getNoWithdrawalAmount() {
        return noWithdrawalAmount;
    }

    public void setNoWithdrawalAmount(BigDecimal noWithdrawalAmount) {
        this.noWithdrawalAmount = noWithdrawalAmount;
    }

    public BigDecimal getShowChange() {
        return showChange;
    }

    public void setShowChange(BigDecimal showChange) {
        this.showChange = showChange;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public Integer getType() {
        return type;
    }

    public BigDecimal getWaitAmount() {
        return waitAmount;
    }

    public void setWaitAmount(BigDecimal waitAmount) {
        this.waitAmount = waitAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
