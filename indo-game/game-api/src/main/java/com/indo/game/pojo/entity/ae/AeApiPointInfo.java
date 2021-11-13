package com.indo.game.pojo.entity.ae;

import java.math.BigDecimal;

/**
 * 上下分对象
 */
public class AeApiPointInfo {

    private BigDecimal point;
    private BigDecimal curr_point;

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public BigDecimal getCurr_point() {
        return curr_point;
    }

    public void setCurr_point(BigDecimal curr_point) {
        this.curr_point = curr_point;
    }

    @Override
    public String toString() {
        return "AeApiPointInfo{" +
                "point=" + point +
                ", curr_point=" + curr_point +
                '}';
    }
}
