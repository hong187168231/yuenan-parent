package com.indo.game.pojo.vo.callback.ps;


import lombok.Data;

@Data
public class PsCallBackResponse {
    private Integer status_code;
    private Integer member_id;
    private String member_name;
    private Integer balance;
}
