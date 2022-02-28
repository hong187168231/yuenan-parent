package com.indo.game.pojo.dto.jdb;

import lombok.Data;

@Data
public class JdbApiGetBalanceRequestData extends JdbApiRequestParentData{
    private String currency;// 货币别
}
