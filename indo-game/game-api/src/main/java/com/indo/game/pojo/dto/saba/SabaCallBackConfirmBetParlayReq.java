package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.util.List;

@Data
public class SabaCallBackConfirmBetParlayReq extends SabaCallBackParentReq{
    private String operationId;// 交易纪录 id
    private String updateTime;// 更新时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private String transactionTime;//下注时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private List<ConfirmBetParlayTicketInfoReq> txns;
    private List<ConfirmBetParlayTicketDetailReq> ticketDetail;

}
