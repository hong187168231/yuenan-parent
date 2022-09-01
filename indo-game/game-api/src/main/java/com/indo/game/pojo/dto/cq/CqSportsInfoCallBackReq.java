package com.indo.game.pojo.dto.cq;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CqSportsInfoCallBackReq {

    /**
     * 混合碼
     */
    @JSONField(name = "mtcode")
    private String mtcode;

    /**
     * 金額
     */
    @JSONField(name = "amount")
    private BigDecimal amount;

    /**
     * 注單號
     */
    @JSONField(name = "roundid")
    private String roundid;

    /**
     * 事件時間
     */
    @JSONField(name = "eventtime")
    private String eventtime;

}
