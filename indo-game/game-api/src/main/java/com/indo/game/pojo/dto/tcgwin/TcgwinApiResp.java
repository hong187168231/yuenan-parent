package com.indo.game.pojo.dto.tcgwin;

import lombok.Data;

import java.util.List;


@Data
public class TcgwinApiResp {
    private int status;
    private String error_desc;
    private String game_url;

    private String pt_username;

    private String pt_password;


}
