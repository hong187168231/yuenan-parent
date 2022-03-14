package com.indo.game.pojo.dto.t9;


import lombok.Data;

@Data
public class T9ApiResponseData {

    private String statusCode;  // 200正常
    private Object data;//结果集
    private String errorMessage;//状态结果集

}
