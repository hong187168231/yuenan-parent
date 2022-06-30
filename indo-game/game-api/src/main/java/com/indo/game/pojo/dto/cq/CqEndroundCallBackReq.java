package com.indo.game.pojo.dto.cq;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CqEndroundCallBackReq extends CqCallBackParentReq {

    @JSONField(name = "account")
    private String account;//	string最大長度為36字元	必填	使用者帳號
    @JSONField(name = "gamehall")
    private String gamehall;//	string最大長度為36字元	必填	遊戲廠商代號
    @JSONField(name = "gamecode")
    private String gamecode;//	string最大長度為36字元	必填	遊戲代號
    @JSONField(name = "roundid")
    private String roundid;//	string最大長度為50字元	必填	注單號
    @JSONField(name = "data")
    private String[] data;//	text array長度不限，但每筆win最大長度為158字元	必填	事件資料列表用JSON包起來
    @JSONField(name = "createTime")
    private String createTime;//	string最大長度為35字元	必填	事件時間 格式為 RFC3339如 2017-01-19T22:56:30-04:00此時間可與注單的createtime對應
    @JSONField(name = "freegame")
    private BigDecimal freegame;//	number	選填	免費遊戲次數
    @JSONField(name = "jackpot")
    private BigDecimal jackpot;//	number	選填	彩池獎金
    @JSONField(name = "jackpotcontribution")
    private BigDecimal jackpotcontribution;//	number array	選填	彩池獎金貢獻值

}
