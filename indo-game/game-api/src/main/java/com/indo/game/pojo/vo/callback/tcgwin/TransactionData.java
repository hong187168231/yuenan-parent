package com.indo.game.pojo.vo.callback.tcgwin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TransactionData {

    private String ref_no;

    private String username;

    private BigDecimal amount;

    private String tx_type;

    private String remarks;

    private Object additionalInformation;
}
