package com.indo.game.pojo.vo.callback.ug;

import lombok.Data;

import java.util.List;

@Data
public class UgCallBackTransferResp<T> extends UgCallBackParentResp {
    private List<T> Balance;
}
