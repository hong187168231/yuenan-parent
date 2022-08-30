package com.indo.game.pojo.vo.callback.sgwin;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class SGWinCallBackParentReq {
    /**
     * 使用者帳號
     */
    @JSONField(name = "account")
    private String account;
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
     * 注單號
     */
    @JSONField(name = "roundid")
    private String roundid;
    /**
     * 混合碼
     */
    @JSONField(name = "mtcode")
    private String mtcode;

    /**
     * 金額
     */
    @JSONField(name = "amount")
    private Double amount;


}
