package com.indo.game.pojo.dto.jdb;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JdbApiRequestGetTryTokenDto extends JdbApiRequestParentDto{
    private String lang;//  String(2) N 语系en：英文（默认值）   cn：简体中文 th：泰文 vn：越南文   ※尚未支援该语系的游戏预设将以英文开启
    private String gType;//  String(2) N 游戏型态
    private String mType;//  String(5) N 机台类型
    private String windowMode;//  String(1) N 1: 使用 JDB 游戏大厅（默认值）  ※若未带入 gType 及 mType，则直接到游戏大 厅   ※若带入 gType 及 mType 时，直接进入游戏。2: 不使用 JDB 游戏大厅 ※gType 及 mType 为必填字段。
    private Boolean isAPP;//  Boolean N 是否为手机 APP 进入游戏  true：手机 APP   false：手机网页、计算机网页（默认值）
    private String lobbyURL;//  String(1000) N 游戏大厅网址  当 windowMode 为 2 时, 此参数才会有作用
    private Integer moreGame;//  Integer N  0: 不显示更多游戏   1: 显示更多游戏（默认值）
    private Integer mute;//  Integer N 默认音效开关   0: 开启音效（默认值）    1: 静音
    private Boolean isShowDollarSign;//  Boolean N 是否显示币别符号 true：显示币别符号 false：不显示币别符号
}
