package com.indo.game.pojo.dto.cq;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class CqSportsCallBackReq<T> {


    /**
     * 使用者帳號
     */
    @JSONField(name = "account")
    private String account;

    /**
     * 驗證 Token
     */
    @JSONField(name = "session")
    private String session;

    /**
     * 遊戲廠商代號
     */
    @JSONField(name = "gamehall")
    private String gamehall;
    /**
     * 遊戲代號
     */
    @JSONField(name = "gamecode")
    private String gamecode;

    /**
     * 事件資料列表用JSON包起來
     */
    @JSONField(name = "data")
    private List<T> data;

    /**
     * 事件時間 格式為 RFC3339
     * 如 2017-01-19T22:56:30-04:00
     * 此時間可與注單的createtime對應
     */
    @JSONField(name = "createTime")
    private String createTime;

}
