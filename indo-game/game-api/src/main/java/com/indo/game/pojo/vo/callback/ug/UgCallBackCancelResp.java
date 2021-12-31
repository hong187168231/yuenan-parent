package com.indo.game.pojo.vo.callback.ug;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


public class UgCallBackCancelResp extends UgCallBackParentResp {
    private String Account;//帐号
    private String TransactionNo;//交易号

    @JSONField(name="TransactionNo")
    public String getAccount() {
        return Account;
    }

    @JSONField(name="TransactionNo")
    public void setAccount(String account) {
        Account = account;
    }

    @JSONField(name="TransactionNo")
    public String getTransactionNo() {
        return TransactionNo;
    }

    @JSONField(name="TransactionNo")
    public void setTransactionNo(String transactionNo) {
        TransactionNo = transactionNo;
    }
}
