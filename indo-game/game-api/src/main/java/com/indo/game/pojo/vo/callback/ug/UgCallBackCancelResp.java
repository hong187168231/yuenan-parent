package com.indo.game.pojo.vo.callback.ug;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UgCallBackCancelResp extends UgCallBackParentResp {
    private String Account;//帐号
    private String TransactionNo;//交易号

}
