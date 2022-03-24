package com.indo.game.pojo.dto.jili;

import lombok.Data;

@Data
public class JiliApiResponse {
    private Integer ErrorCode;  // 200正常
    private Object data;//结果集
    private String Message;//状态结果集
}
