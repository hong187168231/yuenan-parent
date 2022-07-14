package com.indo.game.pojo.vo.callback.bl;


import lombok.Data;

@Data
public class BlResponseParentData<T> {
    private BlMsgResponseData resp_msg;
    private BlResponseData resp_data;

}
