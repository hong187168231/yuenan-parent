package com.indo.game.pojo.dto.pp;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class PpAuthenticateResp extends PpCommonResp {

    private String userId;  // 娱乐场运营商系统中的用户标识符。
    private String currency;//玩家的货币。
    private BigDecimal cash;//玩家的真钱余额。
    private BigDecimal bonus;  // 玩家的奖励余额

    // 玩家的令牌/会话。可以配置为在所有其他API 调用中返回，所以运营商可以在游戏过程中对其加以控制
    private String token;
    /*
    一种可选元素。如何使用这种元素取决于供应商，例如，它可能对游戏中已经存在的限制作进一步的限制
    defaultBet–默认赌注值
    minBet–最小单个赌注金额
    maxBet–最大单个赌注金额
    maxTotalBet–最大总赌注金额（游戏币）
    maxTotalBet–最小总赌注金额（游戏币）
    对游戏内购买功能可选：
    extMinTotalBet–游戏中购买功能的最小总赌注金额（游戏币）
    extMaxTotalBet–游戏中购买功能的最大总赌注金额（游戏币）
    如果extMinTotalBet 和extMaxTotalBet 未包含在响应中，则会使用游戏内购买功能的默认值该字段为可选，
    并且不会默认发送给娱乐场运营商。如果娱乐场运营商需要随请求发送此参数，则他们应要求Pragmatic Play 的技术支持人员进行额外的配置。
     */
    private String betLimits;
    /*
     用于不同目的的附加参数的可选元素集，例如满足管辖权要求
     "promoAvailable":"y" -玩家可以参加促销活动•
     "promoAvailable":"n" -玩家不得参加促销活动示例：
     "extraInfo”:{"promoAvailable":"y"}
     */
    private String extraInfo;

}
