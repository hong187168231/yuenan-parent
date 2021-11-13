package com.indo.game.pojo.entity.ae;

import java.util.List;

/**
 * ae 订单记录结果
 */
public class AeApiOrderResult {

    private Integer total_count;
    private Integer page_num;
    private Integer per_num;
    private List<AeApiOrderInfo> orderInfoList;

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Integer getPage_num() {
        return page_num;
    }

    public void setPage_num(Integer page_num) {
        this.page_num = page_num;
    }

    public Integer getPer_num() {
        return per_num;
    }

    public void setPer_num(Integer per_num) {
        this.per_num = per_num;
    }

    public List<AeApiOrderInfo> getOrderInfoList() {
        return orderInfoList;
    }

    public void setOrderInfoList(List<AeApiOrderInfo> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    @Override
    public String toString() {
        return "AeApiOrderResult{" +
                "total_count=" + total_count +
                ", page_num=" + page_num +
                ", per_num=" + per_num +
                ", orderInfoList=" + orderInfoList +
                '}';
    }
}
