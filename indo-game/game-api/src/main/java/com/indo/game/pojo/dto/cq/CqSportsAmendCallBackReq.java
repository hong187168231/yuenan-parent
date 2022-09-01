package com.indo.game.pojo.dto.cq;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CqSportsAmendCallBackReq<T> {


    /**
     * 使用者帳號
     */
    @JSONField(name = "account")
    private String account;

    /**
     * 事件資料列表用JSON包起來
     */
    @JSONField(name = "data")
    private List<T> data;

    /**
     * 遊戲編碼
     */
    @JSONField(name = "gamecode")
    private String gamecode;

    /**
     * 遊戲廠商代號
     */
    @JSONField(name = "gamehall")
    private String gamehall;

    /**
     * 批次修改後所執行的錢包行為。(確定此批交易是補款還是扣款) 分別為 credit 補款 ,debit 扣款 ，剛好為0時為credit
     */
    @JSONField(name = "action")
    private String action;

    /**
     * 事件時間 格式為 RFC3339
     * 如 2017-01-19T22:56:30-04:00
     * 此時間可與注單的createtime對應
     */
    @JSONField(name = "createTime")
    private String createTime;

    /**
     * 該批交易金額
     * ※金額不得為負值，當為負值時則反回錯誤編碼1003
     */
    @JSONField(name = "amount")
    private BigDecimal amount;
}
