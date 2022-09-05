package com.indo.game.pojo.vo.callback.tcgwin;

import lombok.Data;

@Data
public class TCGWinGetBalanceCallBackReq {

    private String method;//	Y	String	parameter = sgb is a constant
//            数值sgb是一个字串常数
    private String username;//	Y	String
//    merchant player username (min 4 chars , 14 chars max) must be at least 4 up to 14 characters and 0-9, a-z
//
//    游戏账号的登录名( 4 ~ 14位 ) 不能使用特殊字符，由 4 到 14 位数字或小写字母组成

    private int product_type;//	Y	Int
//    refer to Appendix Product Type-> Abbrev
//
//            请参阅附件产品代码

    private Long request_time;//	Y	Long
//    Timestamp of transaction(millisecond)
//
//    交易时间戳（毫秒）

}
