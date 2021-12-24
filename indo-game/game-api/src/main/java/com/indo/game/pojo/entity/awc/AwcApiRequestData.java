package com.indo.game.pojo.entity.awc;

public class AwcApiRequestData<T>{
    private String action;
    private T txns;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getTxns() {
        return txns;
    }

    public void setTxns(T txns) {
        this.txns = txns;
    }
}
