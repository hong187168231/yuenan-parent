package com.indo.game.pojo.dto.dg;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DgMemberCallBackReq {

    @JSONField(name = "username")
    private String username;

    @JSONField(name = "password")
    private String password;

    @JSONField(name = "amount")
    private BigDecimal amount;

    @JSONField(name = "balance")
    private BigDecimal balance;

    @JSONField(name = "winLimit")
    private BigDecimal winLimit;

    @JSONField(name = "status")
    private Integer status;
}
