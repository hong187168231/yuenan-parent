package com.indo.pay.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.mapper.MemTradingMapper;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.pay.mapper.SafeboxRecordMapper;
import com.indo.pay.pojo.resp.SafeboxMoneyResp;
import com.indo.pay.pojo.resp.SafeboxRecord;
import com.indo.pay.service.ISafeBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Service
public class SafeboxServiceImpl extends SuperServiceImpl<SafeboxRecordMapper, SafeboxRecord> implements ISafeBoxService {

    @Autowired
    private IMemGoldChangeService iMemGoldChangeService;
    @Resource
    private SafeboxRecordMapper safeboxRecordMapper;

    /**
     * 增加一条记录用户保险箱金额到用户余额
     */
    @Override
    @Transactional
    public void insertSafeboxRecord(SafeboxRecord record) {
        //更新保险箱金额
        SafeboxMoneyResp boxMoney = new SafeboxMoneyResp();
        boxMoney.setUserId(record.getUserId());
        boxMoney.setUserSafemoney(record.getAfterAmount());

        updateUserSafeboxMoney(boxMoney);

        // 插入保险箱余额转入转出记录
        safeboxRecordMapper.insertSafeboxRecord(record);

        // 更新用户余额并插入用户余额改变记录
        MemGoldChangeDTO goldChangeDO = new MemGoldChangeDTO();
        //用户改变余额是取反
        BigDecimal changeAmount = record.getChangeAmount().multiply(new BigDecimal(-1));

        if (record.getSafeOrdertype().equals(1)) {
            goldChangeDO.setTradingEnum(TradingEnum.SPENDING);
            goldChangeDO.setGoldchangeEnum(GoldchangeEnum.SAFEBOXSAVE);
            goldChangeDO.setChangeAmount(record.getChangeAmount());
        } else {
            goldChangeDO.setTradingEnum(TradingEnum.INCOME);
            goldChangeDO.setGoldchangeEnum(GoldchangeEnum.SAFEBOXDRAW);
            goldChangeDO.setChangeAmount(record.getChangeAmount());
        }

        goldChangeDO.setUserId(record.getUserId().longValue());

        iMemGoldChangeService.updateMemGoldChange(goldChangeDO);


    }

    /**
     * 修改用户保险箱金额
     */
    @Override
    @Transactional
    public void updateUserSafeboxMoney(SafeboxMoneyResp req) {
        safeboxRecordMapper.updateUserSafeboxMoney(req);
    }
    /**
     * 增加一条用户保险箱金额
     */
    @Override
    public void insertUserSafeboxMoney(SafeboxMoneyResp req) {
        safeboxRecordMapper.insertUserSafeboxMoney(req);
    }


    /**
     * 查询用户保险箱和用户余额
     * */
    @Override
    public SafeboxMoneyResp checkSafeboxBalance(Long userid) {
        //通过Authorization获取userid
        System.out.println("checkSafeboxBalance:"+userid);
        //查询用户余额

        //查询获取用户保险金余额
        SafeboxMoneyResp safeboxMoneyResp = safeboxRecordMapper.checkSafeboxBalance(userid);

        //为null则创建一条数据
        if (safeboxMoneyResp == null){

            SafeboxMoneyResp userMoney = new SafeboxMoneyResp();
            userMoney.setUserId(userid.intValue());
            userMoney.setUserSafemoney(new BigDecimal(0));

            insertUserSafeboxMoney(userMoney);

            return userMoney;
        }

        //不为null返回数据
        return safeboxMoneyResp;
    }

    @Override
    public Page<SafeboxRecord> selectSafeboxRecordById(Long userid) {
        return safeboxRecordMapper.selectSafeboxRecordById(userid);
    }
}
