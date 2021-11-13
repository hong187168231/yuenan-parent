package com.indo.game.services;


import com.indo.game.pojo.entity.SysChessBalance;

import java.math.BigDecimal;

public interface SysChessBalanceService {

    /**
     * 获取
     *
     * @param chessCode
     * @return
     */
    SysChessBalance getSysChessBalance(String chessCode);


    /**
     * 增加额度(下分)
     *
     * @param chessCode
     * @param balance
     * @return
     */
    int addBalance(String chessCode, BigDecimal balance);


    /**
     * 减去额度（上分）
     *
     * @param chessCode
     * @param balance
     * @return
     */
    int subtractBalance(String chessCode, BigDecimal balance);


    /**
     * 判断该金额是否足够
     * @param chessCode
     * @param balance
     * @return
     */
    boolean isBalanceInChess(String chessCode, BigDecimal balance);

}
