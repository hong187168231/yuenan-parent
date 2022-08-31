package com.indo.game.pojo.dto.ag;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgCallBackTransfer {
    @JacksonXmlProperty(localName = "sessionToken")
    private String sessionToken;//服务器的验证编码
    @JacksonXmlProperty(localName = "currency")
    private String currency;// 所需币种
    @JacksonXmlProperty(localName = "value")
    private BigDecimal value;// 金额
    @JacksonXmlProperty(localName = "validBetAmount")
    private BigDecimal validBetAmount;// 投注金额
    @JacksonXmlProperty(localName = "netAmount")
    private BigDecimal netAmount;// 贏/输金额
    @JacksonXmlProperty(localName = "playname")
    private String playname;//玩家参数, playname 設定為 product id+username組合, 並区分大小写 e.g. playname: B17test123 (B17 是 product id, test123 是 username)
    @JacksonXmlProperty(localName = "agentCode")
    private String agentCode;// B17
    @JacksonXmlProperty(localName = "betTime")
    private String betTime;// 06/12/2015 11:55:54 格式: 月/日/年 时:分:秒
    @JacksonXmlProperty(localName = "settletime")
    private String settletime;// 06/12/2015 11:55:54 格式: 月/日/年 时:分:秒
    @JacksonXmlProperty(localName = "transactionID")
    private String transactionID;// 同一局里下注同一位置(playtype)兩次會產生兩組交易編號transactionID
    @JacksonXmlProperty(localName = "billNo")
    private String billNo;// 同一局里下注同一位置(playtype)兩次會產生兩組交易編號transactionID
    @JacksonXmlProperty(localName = "platformType")
    private String platformType;// AGIN AGIN= AG 国际厅平台
    @JacksonXmlProperty(localName = "round")
    private String round;// DSP
//    DSP – 国际厅;
//    AGQ – 极速厅;
//    VIP – AG VIP ;
//    LED – 竞咪厅(BID);
//    EMA – 欧洲厅
//    AGNW – 新世界厅

    @JacksonXmlProperty(localName = "gametype")
    private String gametype;//
    //    BAC= 百家乐
//            DT= 龙虎
//    SHB= 骰宝
//            ROU= 轮盘
//    CBAC= 包桌百家乐
//            LBAC= 竞咪百家乐
//    NN= 牛牛
//    更多类型, 请参考附录 D
    @JacksonXmlProperty(localName = "gameCode")
    private String gameCode;// GV0211541508U 游戏局号
    @JacksonXmlProperty(localName = "tableCode")
    private String tableCode;// va21 台号
    @JacksonXmlProperty(localName = "transactionType")
    private String transactionType;// BET
    @JacksonXmlProperty(localName = "transactionCode")
    private String transactionCode;// BCB BCB = 百家乐下注
    @JacksonXmlProperty(localName = "deviceType")
    private String deviceType;// MOBILE 手機版
    @JacksonXmlProperty(localName = "playtype")
    private String playtype;// 1 各游戏玩法请参考附录 D



}
