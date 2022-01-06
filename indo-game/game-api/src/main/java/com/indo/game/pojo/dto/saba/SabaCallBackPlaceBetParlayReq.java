package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SabaCallBackPlaceBetParlayReq extends SabaCallBackParentReq{
    private String operationId;// 交易纪录 id
    private int currency;//沙巴体育货币币别
    private String betTime;//下注时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private String updateTime;// 更新时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private BigDecimal totalBetAmount;//总计注单金额
    private String IP;//
    private String tsId;// 选填，用户登入会话 id，由厂商提供
    private String betFrom;//下注平台。
    private BigDecimal creditAmount;//需增加在玩家的金额。
    private BigDecimal debitAmount;//需从玩家扣除的金额。
    private List<ComboInfo> txns;
    private List<PlaceBetParlayTicketDetailReq> ticketDetail;


}
