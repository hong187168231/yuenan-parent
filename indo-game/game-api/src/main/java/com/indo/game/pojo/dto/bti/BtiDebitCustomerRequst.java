package com.indo.game.pojo.dto.bti;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BtiDebitCustomerRequst {
    private String Parameter;
    private String cust_id;

    private String req_id;

    private BigDecimal amount;
}
