package com.indo.game.pojo.dto.cq;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CqSportsAmendDataCallBackReq {


    /**
     * 混合碼
     */
    @JSONField(name = "mtcode")
    private String mtcode;
    /**
     * 差額部份
     * ※金額不得為負值，當為負值時則反回錯誤編碼1003
     */
    @JSONField(name = "amount")
    private BigDecimal amount;

    /**
     * 批次修改後所執行的錢包行為。(確定此批交易是補款還是扣款) 分別為 credit 補款 ,debit 扣款 ，剛好為0時為credit
     */
    @JSONField(name = "action")
    private String action;

    /**
     * 有效投注
     */
    @JSONField(name = "validbet")
    private String validbet;

    /**
     * 事件時間 time.RFC3339
     */
    @JSONField(name = "eventtime")
    private String eventtime;

    /**
     * 注單號
     */
    @JSONField(name = "roundid")
    private String roundid;

}
