package com.indo.game.pojo.vo.callback.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SabaCallBackPlaceBetResp extends SabaCallBackParentResp {
    private String refId;
    private String licenseeTxId;
}
