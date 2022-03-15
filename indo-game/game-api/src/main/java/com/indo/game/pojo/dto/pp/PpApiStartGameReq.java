package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * PP电子启动游戏请求对象
 */
@Data
public class PpApiStartGameReq {

    /**
     * hash
     */
    @JSONField(name = "hash")
    private String hash;

    /**
     * secureLogin
     */
    @JSONField(name = "secureLogin")
    private String secureLogin;

    /**
     * 用户ID
     */
    @JSONField(name = "externalPlayerId")
    private String externalPlayerId;

    /**
     * 游戏ID
     */
    @JSONField(name = "gameId")
    private String gameId;

    /**
     * 语言。
     */
    @JSONField(name = "language")
    private String language;

    /**
     * 应该用来打开游戏的平台。 从当前开始 可选
     */
    @JSONField(name = "platform")
    private String platform;

    /**
     * 当玩家资金不足时用于打开娱乐场运营商站点上的收银台的链接。可选
     */
    @JSONField(name = "cashierURL")
    private String cashierURL;

    /**
     * 返回到娱乐场运营商站点的大厅页面的链接。此链接用于移动版本游戏中。可选
     */
    @JSONField(name = "lobbyURL")
    private String lobbyURL;

    /**
     * 特定赌桌限额组的标识（仅适用于真人娱乐场产品）可选
     */
    @JSONField(name = "ctlgroup")
    private String ctlgroup;

}
