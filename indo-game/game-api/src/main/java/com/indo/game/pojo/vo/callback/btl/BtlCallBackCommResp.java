package com.indo.game.pojo.vo.callback.btl;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class BtlCallBackCommResp extends BtlCallBackParentErrorResp{
    private String trx_id;
    private BigDecimal balance;
}
