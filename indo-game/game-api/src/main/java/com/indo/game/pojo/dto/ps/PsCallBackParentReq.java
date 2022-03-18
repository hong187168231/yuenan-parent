package com.indo.game.pojo.dto.ps;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class PsCallBackParentReq {
    /**
     * 使用者帳號
     */
    @JSONField(name = "access_token")
    private String access_token;
    /**
     * 单号
     */
    @JSONField(name = "txn_id")
    private String txn_id;
    /**
     * 下注金额
     */
    @JSONField(name = "total_bet")
    private String total_bet;
    /**
     * 主游戏 ID
     */
    @JSONField(name = "game_id")
    private String game_id;
    /**
     * 使用者帳號
     */
    @JSONField(name = "subgame_id")
    private String subgame_id;
    /**
     * 次游戏 ID
     */
    @JSONField(name = "ts")
    private String ts;
    /**
     * 总赢分
     */
    @JSONField(name = "total_win")
    private String total_win;
    /**
     * 免费游戏赢分
     */
    @JSONField(name = "bonus_win")
    private String bonus_win;
    /**
     *彩金贡献金
     */
    @JSONField(name = "jp_contrib")
    private String jp_contrib;

    /**
     * 红利奖金金额
     */
    @JSONField(name = "bonus_reward")
    private String bonus_reward;

}
