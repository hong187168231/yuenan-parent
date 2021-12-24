package com.indo.game.pojo.vo.callback.awc;

import lombok.Data;

@Data
public class AwcGetBalanceRespSuccess extends AwcCallBackParentRespSuccess {
    private String userId;
    private String balance;
    private String balanceTs;
}
