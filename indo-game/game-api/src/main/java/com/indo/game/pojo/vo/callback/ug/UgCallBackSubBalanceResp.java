package com.indo.game.pojo.vo.callback.ug;

import lombok.Data;

import java.util.List;

@Data
public class UgCallBackSubBalanceResp extends UgCallBackParentResp {
    private String userId;//	string	玩家帳號
    private Double balance;//	double	餘額
    private String ticketId;//	string	注單 ID
    private Integer txnId;//	number	交易編號
}
