package com.indo.game.pojo.dto.saba;

import lombok.Data;

@Data
public class ExtraInfo {
    private int gameNo;// Y (int) 游戏期数
    private int range;// Y (int) 指数范围。例如： e.g 5.
    private String gameBetType;// Y (string) 票游戏类别。例如：index big/small

}
