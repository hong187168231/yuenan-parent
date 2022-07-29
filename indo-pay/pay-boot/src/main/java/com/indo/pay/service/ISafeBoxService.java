package com.indo.pay.service;

import com.github.pagehelper.Page;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.pay.pojo.resp.SafeboxMoneyResp;
import com.indo.pay.pojo.resp.SafeboxRecord;
import com.indo.user.pojo.bo.MemTradingBO;

import java.math.BigDecimal;
import java.util.List;

public interface ISafeBoxService {

    /**
     * 增加一条记录用户保险箱金额到用户余额
     */
    void insertSafeboxRecord(SafeboxRecord record);


    /**
     * 修改用户保险箱金额
     */
    void updateUserSafeboxMoney(SafeboxMoneyResp req);

    /**
     * 增加一条用户保险箱金额
     */
    void insertUserSafeboxMoney(SafeboxMoneyResp req);

    /**
     * 查询用户保险箱和用户余额
     * */
    SafeboxMoneyResp checkSafeboxBalance(Long userid);

    /**
     * 查询用户保险箱记录
     * */
    Page<SafeboxRecord> selectSafeboxRecordById(Long userid);

}
