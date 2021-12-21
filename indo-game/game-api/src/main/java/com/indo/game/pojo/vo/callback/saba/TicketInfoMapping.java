package com.indo.game.pojo.vo.callback.saba;

import lombok.Data;

@Data
public class TicketInfoMapping {
    private String refId;//从传入参数取得
    private String licenseeTxId;//厂商系统交易 id
}
