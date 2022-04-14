package com.indo.game.pojo.dto.bl;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BlCallBackReq {


    /**
     * 玩家帐号
     */
    @JSONField(name = "player_account")
    private String player_account;

    /**
     *	子代ID
     */
    @JSONField(name = "operator_id")
    private String operator_id;

    /**
     * 游戏代码
     */
    @JSONField(name = "operator_sub_id")
    private String operator_sub_id;

    /**
     * 游戏牌局编码
     */
    @JSONField(name = "game_code")
    private String game_code;

    /**
     *
     * 游戏牌局编码
     *
     */
    @JSONField(name = "report_id")
    private String report_id;
    /**
     * 余额（玩家在游戏平台内剩余金币）>=0
     */
    @JSONField(name = "amount")
    private BigDecimal amount;

    /**
     * 消耗类型；10：上分，11：上分失败回滚，20：下分
     */
    @JSONField(name = "type")
    private Integer type;
    /**
     *消耗时间戳
     */
    @JSONField(name = "time")
    private String time;
    /**
     * 授权ID（商户提供）
     */
    @JSONField(name = "app_id")
    private String app_id;
    /**
     * 消耗详情(下分)
     */
    @JSONField(name = "cost_info")
    private String cost_info;
    /**
     * sha1加密校验
     */
    @JSONField(name = "sha1")
    private String sha1;


}
