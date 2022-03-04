package com.indo.game.pojo.vo.callback.ae;


import lombok.Data;

@Data
public class AeGetBalanceRespSuccess {
    private String code;
    private String msg;
    private AeGetBalanceResp data;
}
