package com.indo.game.pojo.dto.ug;

import lombok.Data;

@Data
public class UgRegisterPlayerJsonDTO extends UgParentRequJsonDTO {

    private String userId;//	string	50	Y	玩家帐号
    private String loginName;//	string	50	N	玩家昵称，如果不传会取用userId
    private Integer currencyId;//	number		Y	货币代码
    private Integer agentId;//	number		N	代理编号
    private String groupCommission;//	string	1	N	佣金组别，如果不传会设为a 组
}
