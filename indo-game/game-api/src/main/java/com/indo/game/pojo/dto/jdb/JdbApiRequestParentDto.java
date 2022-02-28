package com.indo.game.pojo.dto.jdb;

import lombok.Data;

@Data
public class JdbApiRequestParentDto {
    private Integer action;// Integer 4
    private Long ts;//  Long 当前系统时间
    private String uid;// String(25) 玩家账号，只限 a-z 与 0-9。
    private String parent;// String(19) Y 代理账号
}
