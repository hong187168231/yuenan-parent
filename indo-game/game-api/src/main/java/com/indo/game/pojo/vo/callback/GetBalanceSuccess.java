package com.indo.game.pojo.vo.callback;

import lombok.Data;

@Data
public class GetBalanceSuccess extends CallBackParentSuccess {
    private String userId;
    private String balance;
    private String balanceTs;
}
