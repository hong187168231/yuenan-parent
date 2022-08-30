package com.indo.game.pojo.dto.sgwin;

import lombok.Data;


@Data
public class SgwinLoginRequest {
    private String defaultBgColor;//	String	默认背景色调，white – 白，black – 黑。可空。
//    为空时，默认为白。
    private String defaultColor;//	String	默认色调，blue – 蓝，red – 红，gold – 金，pink – 粉红。可空。
//    为空时，默认为该用户cookies里的色调；
//    如cookies为空时，默认为蓝。
    private String defaultGame;//	String	默认游戏彩票代码, 请参看《附录I》。 可空。
//    为空时，默认为客户口的第一个游戏. 适用于网业手机版
    private String backButton;//	String	显示"返回首页"。可空。
//    When backButton = true显示"返回首页". When click on it, it will redirect base on backURL if given or else The following message will be transmitted over “REDIRECT_HOME”
//    When backButton = false不会显示"返回首页"
//    如果商户对接的模式是点击我方馆场后打开到第二个分页的话，商户无需做任何处理就会看到返回按钮
    private String backUrl;//	String	返回首页的URL。可空。
//    When backUrl with address is given, user will be redirected back to the URL as given
    private Boolean range;//	String	会员可玩的盘口，参数可以不传(新会员默认是分公司有开启的第一个盘口，比如：A盘)或者从A, B, C, D中选一个，老会员则是会默认使用上一次登入时的盘口
//    举例：这个参数传B，会员只能玩B盘口的游戏。可空。
//    editRange		是否要更改老会员的盘口值range。可空。
//            •	如果editRange=true，系统会根据range参数值替换会员的盘口。
//            •	如果无提供editRange此参数值或提供为false，系统不会更改老会员的盘口。
    private Integer tesing;//	Integer	是否为试玩会员。正式:0，试玩：1。可空。
//    为空时，默认为上次该用户登录时的试玩状态；
//    如为第一次登录的用户，默认为正式会员。
//    试玩会员可以转换为正式会员(试玩额度会清零)，而正式会员不可转换。


}
