package com.indo.game.pojo.vo.callback.jdb;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JdbApiResponseData {
    private String status;
    private BigDecimal balance;
}
