package com.indo.game.pojo.vo.callback.sbo;

import lombok.Data;

@Data
public class SboCallBackCommResp extends SboCallBackParentResp{
    private String accountName;
    private String balance;
}
