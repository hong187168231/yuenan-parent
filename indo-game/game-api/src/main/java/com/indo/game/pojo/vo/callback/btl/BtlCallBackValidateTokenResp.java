package com.indo.game.pojo.vo.callback.btl;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class BtlCallBackValidateTokenResp extends BtlCallBackParentErrorResp{
    private String cust_id;
    private String cust_login;
    private String currency_code;
    private BigDecimal balance;
}
