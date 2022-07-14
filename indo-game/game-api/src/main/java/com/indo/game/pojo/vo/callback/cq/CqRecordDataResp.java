package com.indo.game.pojo.vo.callback.cq;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CqRecordDataResp {

    private String _id;
    private String action;
    private BigDecimal before;
    private BigDecimal balance;
    private String currency;
    private CqTargetResp target;
    private CqStatusResp status;
    private List<CqEventDataResp> event;
}
