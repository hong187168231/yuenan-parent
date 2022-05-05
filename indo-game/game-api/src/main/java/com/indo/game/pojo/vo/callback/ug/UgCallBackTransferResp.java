package com.indo.game.pojo.vo.callback.ug;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
@Data
public class UgCallBackTransferResp<T> extends UgCallBackParentResp {
    private List<T> Balance;

    @JSONField(name="Balance")
    public List<T> getBalance() {
        return Balance;
    }

    @JSONField(name="Balance")
    public void setBalance(List<T> balance) {
        Balance = balance;
    }
}
