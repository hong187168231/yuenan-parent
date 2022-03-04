package com.indo.game.pojo.vo.callback.jdb;

import lombok.Data;

import java.util.List;

@Data
public class JdbApiIsGameingRequestBack<T> {
    private String status;
    private String err_text;
    private List<T> data;
}
