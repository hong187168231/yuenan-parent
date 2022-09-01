package com.indo.game.pojo.dto.cq;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class CqSportsWinsCallBackReq<T> {


    /**
     * 使用者帳號
     */
    @JSONField(name = "account")
    private String account;

    /**
     * 該批交易的時間
     */
    @JSONField(name = "eventtime")
    private String eventtime;

    /**
     * 交易批碼 ，回傳派彩結果時需附代此資訊
     */
    @JSONField(name = "ucode")
    private String ucode;

    /**
     * 事件資料列表用JSON包起來
     */
    @JSONField(name = "event")
    private List<T> event;

}
