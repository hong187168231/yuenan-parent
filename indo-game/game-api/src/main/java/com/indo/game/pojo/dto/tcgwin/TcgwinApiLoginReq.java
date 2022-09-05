package com.indo.game.pojo.dto.tcgwin;

import lombok.Data;

import java.util.List;


@Data
public class TcgwinApiLoginReq<T> {

    private String method;//	Y	String	parameter = cm is a constant
//            数值cm是一个字串常数
    private String username;//	Y	String
//    merchant player username (min 4 chars , 14 chars max) must be at least 4 up to 14 characters and 0-9, a-z

//    游戏账号的登录名( 4 ~ 14位 ) 不能使用特殊字符，由 4 到 14 位数字或小写字母组成

    private int product_type;
    private String platform;
    private String game_mode;
    private String game_code;
    private String view;
    private String language;
    private List<T> series;
}
