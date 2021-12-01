package com.indo.game.pojo.vo.callback;

import lombok.Data;

@Data
public class CallBackSuccess extends CallBackParentSuccess {
    private String balance;
    private String balanceTs;
}
