package com.indo.game.pojo.dto.pg;

import com.alibaba.fastjson.annotation.JSONField;
import com.indo.game.pojo.dto.cq.CqCallBackParentReq;

import lombok.Data;

@Data
public class PgVerifyCallBackReq {


    /**
     * 运营商独有的身份识别
     */
    @JSONField(name = "operator_token")
    private String operator_token;

    /**
     * PGSoft 与运营商之间共享密码
     */
    @JSONField(name = "secret_key")
    private String secret_key;

    /**
     * 运营商系统生成的令牌
     */
    @JSONField(name = "operator_player_session")
    private String operator_player_session;

    /**
     * operator_param 值
     */
    @JSONField(name = "custom_parameter")
    private String custom_parameter;


    /**
     * 游戏的独有代码
     */
    @JSONField(name = "game_id")
    private Integer game_id;
    /**
     * 账户
     */
    @JSONField(name = "player_name")
    private String player_name;
}
