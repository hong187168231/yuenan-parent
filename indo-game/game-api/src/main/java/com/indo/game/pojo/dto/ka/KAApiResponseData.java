package com.indo.game.pojo.dto.ka;


import lombok.Data;

@Data
public class KAApiResponseData {

    private Integer statusCode;  // 0正常
    private Object data;//结果集
    private String status;//状态结果集

}
