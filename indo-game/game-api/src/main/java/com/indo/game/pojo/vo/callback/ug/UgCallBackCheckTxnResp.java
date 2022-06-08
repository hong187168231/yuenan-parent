package com.indo.game.pojo.vo.callback.ug;

import lombok.Data;

import java.util.List;

@Data
public class UgCallBackCheckTxnResp<T> extends UgCallBackParentResp {
    private List<T> data;
}
