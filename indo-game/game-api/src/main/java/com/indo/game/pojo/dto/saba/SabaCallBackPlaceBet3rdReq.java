package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.util.List;

@Data
public class SabaCallBackPlaceBet3rdReq<T> extends SabaCallBackParentReq{
    private String operationId;// 交易纪录 id
    private int currency;//  Y (int) 沙巴货币币别 Id。例如： 1, 2, 20
    private int productId;//  Y (int) Sport type
    private int gameId;//  Y (int) 投注类型(Bet type)
    private List<T> ticketList;//  Y (array) 请参阅表 TicketDetail
    private String betTime;//  Y (string) 下注时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private String IP;//  Y (string) 例如： 61.221.35.49 (IPV4)
    private String tsId;//  N (string) 选填，用户登入会话 id，由厂商提供
    private String productName_en;//  Y sport type 英文名称。
    private String gameName_en;//  Y 投注类型(Bet type) 英文名称。
    private String betFrom;//  Y (string)下注平台。

}
