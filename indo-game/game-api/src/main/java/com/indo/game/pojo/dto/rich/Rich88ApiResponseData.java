package com.indo.game.pojo.dto.rich;


import lombok.Data;

@Data
public class Rich88ApiResponseData {

    private Integer code;  // 0正常
    private Object data;//结果集
    private String msg;//状态结果集 默认成功

}
