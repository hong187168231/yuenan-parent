package com.indo.game.pojo.dto.pp;


import lombok.Data;

@Data
public class PpCommonResp {

    private Integer error = 0;  // 0正常, 默认成功
    private String description = "Success"; // 返回信息, 默认成功

}
