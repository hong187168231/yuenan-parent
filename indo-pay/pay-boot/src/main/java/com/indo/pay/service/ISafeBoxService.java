package com.indo.pay.service;

import com.github.pagehelper.Page;
import com.indo.pay.pojo.req.SafeboxMoneyReq;
import com.indo.pay.pojo.resp.SafeboxRecord;

public interface ISafeBoxService {

    /**
     * 增加一条记录用户保险箱金额到用户余额
     */
    void insertSafeboxRecord(SafeboxRecord record);


    /**
     * 修改用户保险箱金额
     */
    void updateUserSafeboxMoney(SafeboxMoneyReq req);

    /**
     * 增加一条用户保险箱金额
     */
    void insertUserSafeboxMoney(SafeboxMoneyReq req);

    /**
     * 查询用户保险箱和用户余额
     * */
    SafeboxMoneyReq checkSafeboxBalance(Long userid);

    /**
     * 查询用户保险箱记录
     * */
    Page<SafeboxRecord> selectSafeboxRecordById(Long userid);

}
