package com.indo.game.pojo.dto.dj;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DjCallBackParentReq {
    /**
     *
     */
    @JSONField(name = "api_key")
    private String api_key;
    /**
     *
     */
    @JSONField(name = "agent_code")
    private String agent_code;
    /**
     *
     */
    @JSONField(name = "ticket_id")
    private String ticket_id;

    @JSONField(name = "login_id")
    private String login_id;
    /**
     *
     */
    @JSONField(name = "match_no")
    private String match_no;
    /**
     *
     */
    @JSONField(name = "fight_datetime")
    private String fight_datetime;
    /**
     *
     */
    @JSONField(name = "bet_on")
    private String bet_on;
    /**
     *
     */
    @JSONField(name = "stake")
    private String stake;
    /**
     * 免费游戏赢分
     */
    @JSONField(name = "stake_money")
    private BigDecimal stake_money;
    /**
     *彩金贡献金
     */
    @JSONField(name = "created_datetime")
    private String created_datetime;



}
