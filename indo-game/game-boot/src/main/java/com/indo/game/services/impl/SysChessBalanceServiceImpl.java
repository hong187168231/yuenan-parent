package com.indo.game.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.game.mapper.SysChessBalanceMapper;
import com.indo.game.pojo.entity.SysChessBalance;
import com.indo.game.services.SysChessBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SysChessBalanceServiceImpl implements SysChessBalanceService {

    private static final Logger logger = LoggerFactory.getLogger(SysChessBalanceServiceImpl.class);

    @Autowired
    private SysChessBalanceMapper sysChessBalanceMapper;
//    @Autowired
//    private SysChessBalanceMapperExt sysChessBalanceMapperExt;


    @Override
    public SysChessBalance getSysChessBalance(String chessCode) {
        LambdaQueryWrapper<SysChessBalance> wrapper = new LambdaQueryWrapper();
        wrapper.eq(SysChessBalance::getIsDelete,false);
        wrapper.eq(SysChessBalance::getChessCode,chessCode);
        SysChessBalance sysChessBalance = sysChessBalanceMapper.selectOne(wrapper);
        return sysChessBalance;
    }

    @Override
    public int addBalance(String chessCode, BigDecimal balance) {
        return sysChessBalanceMapper.addBalance(chessCode, balance);
    }

    @Override
    public int subtractBalance(String chessCode, BigDecimal balance) {
        return sysChessBalanceMapper.subtractBalance(chessCode, balance);
    }

    @Override
    public boolean isBalanceInChess(String chessCode, BigDecimal balance) {
        SysChessBalance sysChessBalance = getSysChessBalance(chessCode);
        if (null == sysChessBalance) {
            logger.error("isBalanceInChess  chessCode:{}  is null", chessCode);
            return false;
        }
        BigDecimal chessBalance = sysChessBalance.getChessBalance();
        logger.info("站点棋牌chessCode={},chessBalance={},用户金额balance={}", chessCode, chessBalance, balance);
        if (chessBalance.compareTo(balance) > (-1)) {
            //棋牌余额大于等于用户余额
            return true;
        }
        return false;
    }
}
