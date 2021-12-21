package com.indo.game.pojo.vo.callback.awc;

import lombok.Data;

@Data
public class AwcCallBackRespSuccess extends AwcCallBackParentRespSuccess {
    private String balance;
    private String balanceTs;
}
