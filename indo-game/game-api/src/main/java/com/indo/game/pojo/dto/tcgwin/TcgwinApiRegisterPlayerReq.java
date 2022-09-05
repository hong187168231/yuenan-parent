package com.indo.game.pojo.dto.tcgwin;

import lombok.Data;


@Data
public class TcgwinApiRegisterPlayerReq {

    private String method;//	Y	String	parameter = cm is a constant
//            数值cm是一个字串常数
    private String username;//	Y	String
//    merchant player username (min 4 chars , 14 chars max) must be at least 4 up to 14 characters and 0-9, a-z

//    游戏账号的登录名( 4 ~ 14位 ) 不能使用特殊字符，由 4 到 14 位数字或小写字母组成

    private String password;//	Y	String
//    merchant player password (min 6 chars , 12 chars max)The special characters are not allowed. Password must be at least 6 up to 12 characters and 0-9, A-z

//    游戏账号的密码( 6 ~ 12位 )不能使用特殊字符，由 6 到 12 位数字或大小写字母组成

    private String currency;//	N	String
//    Please see appendix Currency Code
//
//    請參考附件 币别代码
}
