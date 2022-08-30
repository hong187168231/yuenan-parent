package com.indo.game.service.ag.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.dto.ag.AgCallBackTransfer;
import com.indo.game.pojo.vo.callback.ag.TransferResponse;
import com.indo.game.service.ag.AgCallbackService;
import com.indo.game.service.common.GameCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class AgCallbackServiceImpl implements AgCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    //Place Bet 下注

    public Object bet(AgCallBackTransfer agCallBackTransfer, String ip){
        GamePlatform gamePlatform = new GamePlatform();
        String platformCode = agCallBackTransfer.getGameCode();
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("OK");
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AG_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            transferResponse.setResponseCode("INVALID_DATA");
            return transferResponse;
        }
        String playname = agCallBackTransfer.getPlayname();
        int sp = playname.split(agCallBackTransfer.getAgentCode()).length;
        String userId = playname.substring(sp,playname.length());
        if(OpenAPIProperties.AG_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.AG_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
        }else {
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformCode,gameParentPlatform.getPlatformCode());
        }
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
        if (null == memBaseinfo) {
            transferResponse.setResponseCode("INCORRECT_SESSION_TYPE");
            return transferResponse;
        }
        BigDecimal balance = memBaseinfo.getBalance();

        BigDecimal betAmount = null!=agCallBackTransfer.getValue()?agCallBackTransfer.getValue().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
        if (balance.compareTo(betAmount) == -1) {
            transferResponse.setResponseCode("INSUFFICIENT_FUNDS");
            return transferResponse;
        }
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, agCallBackTransfer.getTransactionID());
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.AG_PLATFORM_CODE);
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            transferResponse.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            return transferResponse;
        }

        balance = balance.subtract(betAmount);
        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

        Txns txns = new Txns();
        //下注金额
        txns.setBetAmount(betAmount);
        //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
        txns.setWinningAmount(betAmount.negate());
        txns.setWinAmount(betAmount);
        txns.setBalance(balance);
        txns.setMethod("Place Bet");
        txns.setStatus("Running");
        txns.setPlatformTxId(agCallBackTransfer.getTransactionID());
        String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
        txns.setCreateTime(dateStr);
        //玩家货币代码
        txns.setCurrency(gameParentPlatform.getCurrencyType());
        //平台代码
        txns.setPlatform(gameParentPlatform.getPlatformCode());
        //平台英文名称
        txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
        //平台中文名称
        txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
        //平台游戏类型
        txns.setGameType(gameCategory.getGameType());
        //游戏分类ID
        txns.setCategoryId(gameCategory.getId());
        //游戏分类名称
        txns.setCategoryName(gameCategory.getGameName());
        //平台游戏代码
        txns.setGameCode(gamePlatform.getPlatformCode());
        //游戏名称
        txns.setGameName(gamePlatform.getPlatformEnName());
        txnsMapper.insert(txns);

        transferResponse.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
        return transferResponse;
    }
    //赢

    public Object win(AgCallBackTransfer agCallBackTransfer, String ip){
        GamePlatform gamePlatform = new GamePlatform();
        String platformCode = agCallBackTransfer.getGameCode();
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("OK");
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AG_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            transferResponse.setResponseCode("INVALID_DATA");
            return transferResponse;
        }
        String playname = agCallBackTransfer.getPlayname();
        int sp = playname.split(agCallBackTransfer.getAgentCode()).length;
        String userId = playname.substring(sp,playname.length());

        if(OpenAPIProperties.AG_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.AG_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
        }else {
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformCode,gameParentPlatform.getPlatformCode());
        }
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
        if (null == memBaseinfo) {
            transferResponse.setResponseCode("INCORRECT_SESSION_TYPE");
            return transferResponse;
        }
        BigDecimal balance = memBaseinfo.getBalance();

        BigDecimal betAmount = null!=agCallBackTransfer.getValue()?agCallBackTransfer.getValue().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
        if (balance.compareTo(betAmount) == -1) {
            transferResponse.setResponseCode("INSUFFICIENT_FUNDS");
            return transferResponse;
        }
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, agCallBackTransfer.getTransactionID());
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.AG_PLATFORM_CODE);
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            transferResponse.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            return transferResponse;
        }

        balance = balance.subtract(betAmount);
        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

        Txns txns = new Txns();
        //下注金额
        txns.setBetAmount(betAmount);
        //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
        txns.setWinningAmount(betAmount.negate());
        txns.setWinAmount(betAmount);
        txns.setBalance(balance);
        txns.setMethod("Place Bet");
        txns.setStatus("Running");
        txns.setPlatformTxId(agCallBackTransfer.getTransactionID());
        String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
        txns.setCreateTime(dateStr);
        //玩家货币代码
        txns.setCurrency(gameParentPlatform.getCurrencyType());
        //平台代码
        txns.setPlatform(gameParentPlatform.getPlatformCode());
        //平台英文名称
        txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
        //平台中文名称
        txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
        //平台游戏类型
        txns.setGameType(gameCategory.getGameType());
        //游戏分类ID
        txns.setCategoryId(gameCategory.getId());
        //游戏分类名称
        txns.setCategoryName(gameCategory.getGameName());
        //平台游戏代码
        txns.setGameCode(gamePlatform.getPlatformCode());
        //游戏名称
        txns.setGameName(gamePlatform.getPlatformEnName());
        txnsMapper.insert(txns);

        transferResponse.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
        return transferResponse;
    }


    private boolean checkIp(GameParentPlatform gameParentPlatform,String ip) {
        if (null == gameParentPlatform) {
            return false;
        } else if (null == gameParentPlatform.getIpAddr() || "".equals(gameParentPlatform.getIpAddr())) {
            return true;
        } else if (gameParentPlatform.getIpAddr().equals(ip)) {
            return true;
        }
        return false;
    }

}
