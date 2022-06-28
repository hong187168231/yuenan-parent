package com.indo.game.pojo.dto.cq;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CqEndroundDataCallBackReq extends CqCallBackParentReq {

    @JSONField(name = "mtcode")
    private String mtcode;//混合碼
    @JSONField(name = "amount")
    private Double amount;//金額
    @JSONField(name = "eventtime")
    private String eventtime;//事件時間 time.RFC3339
    @JSONField(name = "validbet")
    private BigDecimal validbet;//有效投注

}
