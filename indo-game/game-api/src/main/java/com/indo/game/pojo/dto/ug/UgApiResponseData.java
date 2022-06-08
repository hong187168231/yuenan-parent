package com.indo.game.pojo.dto.ug;

import lombok.Data;

@Data
public class UgApiResponseData {
    private String code;//	string	Y	错误代码
    private String msg;//	string	Y	讯息
    private Object data;//	object	N	回应资料
}
