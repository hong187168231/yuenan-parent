package com.indo.game.service.lottery.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.core.pojo.entity.game.GameLotteryRule;
import com.indo.core.pojo.entity.game.GameLotteryWinningNumber;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.mapper.lottery.GameLotteryOrderMapper;
import com.indo.game.mapper.lottery.GameLotteryRuleMapper;
import com.indo.game.mapper.lottery.GameLotteryWinningNumberMapper;
import com.indo.game.pojo.dto.lottery.GameLotteryOrderDto;
import com.indo.game.pojo.entity.lottery.GameLotteryOrder;
import com.indo.core.service.game.common.GameCommonService;
import com.indo.game.service.lottery.YxxService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class YxxServerImpl extends ServiceImpl<GameLotteryWinningNumberMapper, GameLotteryWinningNumber> implements YxxService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Autowired
    private GameLotteryOrderMapper gameLotteryOrderMapper;

    @Autowired
    private GameLotteryRuleMapper gameLotteryRuleMapper;

    /**
     * 订单
     * @param loginUser
     * @param gameLotteryOrderDto
     * @return
     */
    @Override
    public Result yxxOrder(LoginInfo loginUser, GameLotteryOrderDto gameLotteryOrderDto) {
        logger.info("yxxOrder userID:{}, NickName:{}, lotteryDate:{}, gameLotteryOrderDto:{}", loginUser.getId(), loginUser.getNickName(),gameLotteryOrderDto.getLotteryDate(),gameLotteryOrderDto);
        LambdaQueryWrapper<GameLotteryWinningNumber> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameLotteryWinningNumber::getLotteryDate, gameLotteryOrderDto.getLotteryDate());
        GameLotteryWinningNumber gameLotteryWinningNumber = baseMapper.selectOne(wrapper);
        if(null!=gameLotteryWinningNumber){
            if(1==gameLotteryWinningNumber.getIsStatus()){
                return Result.failed("gl00002", "开奖期号已结算不可以投注！");
            }else {
                GameLotteryOrder gameLotteryOrder = new GameLotteryOrder();
                BeanUtils.copyProperties(gameLotteryOrderDto, gameLotteryOrder);
                gameLotteryOrder.setCreateTime(DateUtils.format(new Date(), DateUtils.longFormat));
                gameLotteryOrder.setUserAcct(loginUser.getAccount());
                gameLotteryOrder.setIsStatus(0);
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(loginUser.getAccount());
                //扣除金额
                BigDecimal balance  = memBaseinfo.getBalance().subtract(gameLotteryOrderDto.getOrderAmt());
                gameCommonService.updateUserBalance(memBaseinfo, gameLotteryOrderDto.getOrderAmt(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                gameLotteryOrderMapper.insert(gameLotteryOrder);
                return Result.success();
            }
        }else {
            return Result.failed("gl00001", "开奖期号不能不存在！");
        }
    }

    @Override
    public void yxxOrderSettle(){
        GameLotteryWinningNumber gameLotteryWinningNumber = baseMapper.qeryMinGameLotteryWinningNumber();
        gameLotteryWinningNumber.setIsStatus(2);//结算中
        gameLotteryWinningNumber.setUpdateTime(DateUtils.format(new Date(), DateUtils.longFormat));
        baseMapper.updateById(gameLotteryWinningNumber);
        LambdaQueryWrapper<GameLotteryOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameLotteryOrder::getLotteryDate, gameLotteryWinningNumber.getLotteryDate());
        wrapper.eq(GameLotteryOrder::getIsStatus, 0);
        List<GameLotteryOrder> lotteryOrderList = gameLotteryOrderMapper.selectList(wrapper);
        for(GameLotteryOrder gameLotteryOrder:lotteryOrderList) {
            LambdaQueryWrapper<GameLotteryRule> wrapperRule = new LambdaQueryWrapper<>();
            wrapperRule.eq(GameLotteryRule::getGameCode,gameLotteryOrder.getGameCode());
            wrapperRule.eq(GameLotteryRule::getPlatformCode,gameLotteryOrder.getPlatformCode());
            wrapperRule.eq(GameLotteryRule::getGamePlayCode,gameLotteryOrder.getGamePlayCode());
            GameLotteryRule rule = gameLotteryRuleMapper.selectOne(wrapperRule);
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(gameLotteryOrder.getUserAcct());
            if(!(gameLotteryWinningNumber.getOneWinNum()==gameLotteryWinningNumber.getTwoWinNum()&&gameLotteryWinningNumber.getOneWinNum()==gameLotteryWinningNumber.getThreeWinNum())){
                boolean b = false;//是否中奖
                //单色
                if(gameLotteryOrder.getWinningNumber()==gameLotteryWinningNumber.getOneWinNum()
                        ||gameLotteryOrder.getWinningNumber()==gameLotteryWinningNumber.getTwoWinNum()
                        ||gameLotteryOrder.getWinningNumber()==gameLotteryWinningNumber.getThreeWinNum()){
                    b = true;
                }
                //同色
                //双色
                if(gameLotteryOrder.getWinningNumber()==Integer.valueOf(String.valueOf(gameLotteryWinningNumber.getOneWinNum())+String.valueOf(gameLotteryWinningNumber.getTwoWinNum()))
                        ||gameLotteryOrder.getWinningNumber()==Integer.valueOf(String.valueOf(gameLotteryWinningNumber.getTwoWinNum())+String.valueOf(gameLotteryWinningNumber.getThreeWinNum()))
                        ||gameLotteryOrder.getWinningNumber()==Integer.valueOf(String.valueOf(gameLotteryWinningNumber.getOneWinNum())+String.valueOf(gameLotteryWinningNumber.getThreeWinNum()))){

                    b = true;
                }
                if(b){
                    BigDecimal winAmt = gameLotteryOrder.getOrderAmt().multiply(rule.getOdds()).setScale(2);
                    //扣除金额
                    BigDecimal balance  = memBaseinfo.getBalance().add(winAmt);
                    gameCommonService.updateUserBalance(memBaseinfo, winAmt, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                    gameLotteryOrder.setWinAmt(winAmt);
                }
            }
            gameLotteryOrder.setIsStatus(1);
            gameLotteryOrder.setUpdateTime(DateUtils.format(new Date(), DateUtils.longFormat));
            gameLotteryOrderMapper.updateById(gameLotteryOrder);
        }
        gameLotteryWinningNumber.setUpdateTime(DateUtils.format(new Date(), DateUtils.longFormat));
        gameLotteryWinningNumber.setIsStatus(2);//结算完成
        baseMapper.updateById(gameLotteryWinningNumber);
    }
}
