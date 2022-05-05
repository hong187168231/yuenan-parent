package com.indo.game.pojo.vo.callback.ug;

import lombok.Data;

@Data
public class UgCallBackCancelSubResp {
    private String code;//	string	錯誤代碼
    private String msg;//	string	訊息
    private String userId;//	string	50	Y	玩家帳號
    private String txnId;//	number		Y	交易編號
}
