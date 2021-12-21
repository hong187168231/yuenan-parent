package com.indo.game.pojo.vo.callback.saba;

import lombok.Data;

import java.util.List;

@Data
public class SabaCallBackPlaceBetParlayResp<T> extends SabaCallBackParentResp {
    private List<T> txns;
}
