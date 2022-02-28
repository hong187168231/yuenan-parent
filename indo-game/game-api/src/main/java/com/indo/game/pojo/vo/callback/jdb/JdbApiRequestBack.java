package com.indo.game.pojo.vo.callback.jdb;

import lombok.Data;

@Data
public class JdbApiRequestBack {
    private String status;
    private String err_text;
    private String path;// String(255) 登入 URL
}
