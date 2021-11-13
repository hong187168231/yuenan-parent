package com.indo.game.services;


import com.indo.game.pojo.dto.MemGoldchangeDO;


public interface MemBaseinfoWriteService {

    /**
     * 修改用户余额
     *
     * @param change 改变记录
     * @return
     */
    boolean updateUserBalance(MemGoldchangeDO change) throws RuntimeException;

}
