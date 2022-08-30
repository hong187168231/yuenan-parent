package com.indo.game.pojo.dto.sgwin;

import lombok.Data;


@Data
public class SgwinApiResp {
    private Boolean success;
    private String result;
    private String error;
    private String message;
}
