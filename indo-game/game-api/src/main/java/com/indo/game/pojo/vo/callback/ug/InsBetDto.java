package com.indo.game.pojo.vo.callback.ug;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class InsBetDto {
    private long InsID;// long 是 保险单编号
    private BigDecimal BetValue;// decimal 是 出售金额
    private BigDecimal BackValue;// decimal 是 返还金额
    private String BetDate;// datetime 是 出售日期
    private BigDecimal Win;// decimal 是 输赢金额
    @JSONField(name="InsID")
    public long getInsID() {
        return InsID;
    }
    @JSONField(name="InsID")
    public void setInsID(long insID) {
        InsID = insID;
    }
    @JSONField(name="BetValue")
    public BigDecimal getBetValue() {
        return BetValue;
    }
    @JSONField(name="BetValue")
    public void setBetValue(BigDecimal betValue) {
        BetValue = betValue;
    }
    @JSONField(name="BackValue")
    public BigDecimal getBackValue() {
        return BackValue;
    }
    @JSONField(name="BackValue")
    public void setBackValue(BigDecimal backValue) {
        BackValue = backValue;
    }
    @JSONField(name="BetDate")
    public String getBetDate() {
        return BetDate;
    }
    @JSONField(name="BetDate")
    public void setBetDate(String betDate) {
        BetDate = betDate;
    }
    @JSONField(name="Win")
    public BigDecimal getWin() {
        return Win;
    }
    @JSONField(name="Win")
    public void setWin(BigDecimal win) {
        Win = win;
    }
}
