package com.indo.game.pojo.dto.ug;

import lombok.Data;

@Data
public class UgCallBackCheckLoginReq extends UgCallBackParentReq {

    private String userId;//	string	50	Y	玩家帳號
    private String token;//	string	256	N	接入方自定义玩家登入验证 token

}
