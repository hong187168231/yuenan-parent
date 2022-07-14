package com.indo.game.service.pg.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.encrypt.Base64;
import com.indo.common.utils.encrypt.MD5;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.ae.AeCallBackTransferReq;
import com.indo.game.pojo.dto.pg.PgVerifyCallBackReq;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.vo.callback.ae.AeCallBackRespFail;
import com.indo.game.pojo.vo.callback.ae.AeGetBalanceResp;
import com.indo.game.pojo.vo.callback.ae.AeGetBalanceRespSuccess;
import com.indo.game.pojo.vo.callback.pg.PgCallBackResponse;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.pg.PgCallbackService;
import com.indo.user.pojo.bo.MemTradingBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;


/**
 * PG
 *
 * @author
 */
@Service
public class PgCallbackServiceImpl implements PgCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object pgBalanceCallback(PgVerifyCallBackReq pgVerifyCallBackReq, String ip) {
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.PG_PLATFORM_CODE);
        //进行秘钥
        PgCallBackResponse pgCallBackRespFail = new PgCallBackResponse();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(pgVerifyCallBackReq.getPlayer_name());
        JSONObject dataJson = new JSONObject();
        JSONObject errorJson = new JSONObject();
        if (null == memBaseinfo) {
            pgCallBackRespFail.setData(dataJson);
            errorJson.put("code", "1034");
            errorJson.put("message", "无效请求");
            pgCallBackRespFail.setData(null);
            pgCallBackRespFail.setError(errorJson);
            return pgCallBackRespFail;
        } else {
            long currentTime = System.currentTimeMillis();
            dataJson.put("updated_time", currentTime);
            dataJson.put("balance_amount", memBaseinfo.getBalance());
            dataJson.put("currency_code", platformGameParent.getCurrencyType());
            pgCallBackRespFail.setData(dataJson);
            pgCallBackRespFail.setError(null);
            return pgCallBackRespFail;
        }
    }

    @Override
    public Object pgTransferInCallback(PgVerifyCallBackReq pgVerifyCallBackReq, String ip) {
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(pgVerifyCallBackReq.getPlayer_name());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.PG_PLATFORM_CODE);
        GamePlatform gamePlatform;
        if(OpenAPIProperties.PG_IS_PLATFORM_LOGIN.equals("Y")) {
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(), gameParentPlatform.getPlatformCode());
        }else {
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(String.valueOf(pgVerifyCallBackReq.getGame_id()), gameParentPlatform.getPlatformCode());
        }
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        PgCallBackResponse pgCallBackRespFail = new PgCallBackResponse();
        JSONObject dataJson = new JSONObject();
        JSONObject errorJson = new JSONObject();
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal transferAmount = pgVerifyCallBackReq.getTransfer_amount();
        BigDecimal betAmount = pgVerifyCallBackReq.getBet_amount();
        if (memBaseinfo.getBalance().compareTo(transferAmount) == -1) {
            errorJson.put("code", "1018");
            errorJson.put("message", "ot Enough Balance");
            pgCallBackRespFail.setData(null);
            pgCallBackRespFail.setError(errorJson);
            return pgCallBackRespFail;
        }
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, pgVerifyCallBackReq.getBet_id());
        wrapper.eq(Txns::getPlatform,gameParentPlatform.getPlatformCode());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                errorJson.put("code", "1034");
                errorJson.put("message", "无效请求");
                pgCallBackRespFail.setData(null);
                pgCallBackRespFail.setError(errorJson);
                return pgCallBackRespFail;
            }
        }
        balance = balance.add(transferAmount);
        if (transferAmount.compareTo(BigDecimal.ZERO) != 0) {
            if (betAmount.compareTo(BigDecimal.ZERO) == 0) {//下注
                gameCommonService.updateUserBalance(memBaseinfo, transferAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
            } else {
                if (betAmount.compareTo(BigDecimal.ZERO) == -1) {//输
                    gameCommonService.updateUserBalance(memBaseinfo, transferAmount.abs(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                } else {
                    gameCommonService.updateUserBalance(memBaseinfo, transferAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                }
            }
        }

        Txns txns = new Txns();
        //创建时间
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        if (oldTxns != null) {
            BeanUtils.copyProperties(oldTxns, txns);
            oldTxns.setStatus("Settle");
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);
        }else {
            //游戏商注单号
            txns.setPlatformTxId(pgVerifyCallBackReq.getBet_id());
            //此交易是否是投注 true是投注 false 否
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
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
            //操作名称
            txns.setMethod("Place Bet");
            txns.setStatus("Running");
        }
        //下注金额
        txns.setBetAmount(betAmount);
        //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
        txns.setWinningAmount(transferAmount);
        txns.setWinAmount(pgVerifyCallBackReq.getWin_amount());
        //玩家下注时间
        txns.setBetTime(DateUtils.formatByLong(pgVerifyCallBackReq.getCreate_time(), DateUtils.newFormat));
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(pgVerifyCallBackReq.getBet_amount());
        //真实返还金额,游戏赢分
        txns.setRealWinAmount(pgVerifyCallBackReq.getTransfer_amount());
        //返还金额 (包含下注金额)
        //赌注的结果 : 赢:0,输:1,平手:2
        int resultTyep;
        if (pgVerifyCallBackReq.getWin_amount().compareTo(BigDecimal.ZERO) == 0) {
            resultTyep = 2;
        } else if (pgVerifyCallBackReq.getWin_amount().compareTo(BigDecimal.ZERO) == 1) {
            resultTyep = 0;
        } else {
            resultTyep = 1;
        }
        txns.setResultType(resultTyep);
        //有效投注金额 或 投注面值
        txns.setTurnover(pgVerifyCallBackReq.getBet_amount());
        //辨认交易时间依据
        txns.setTxTime(DateUtils.formatByLong(pgVerifyCallBackReq.getCreate_time(), DateUtils.newFormat));

        //余额
        txns.setBalance(balance);

        txns.setCreateTime(dateStr);
        //投注 IP
        txns.setBetIp(ip);//  string 是 投注 IP

        int num = txnsMapper.insert(txns);
        if (num <= 0) {
            pgCallBackRespFail.setData(dataJson);
            errorJson.put("code", "1034");
            errorJson.put("message", "无效请求");
            pgCallBackRespFail.setData(null);
            pgCallBackRespFail.setError(errorJson);
            return pgCallBackRespFail;
        }

        dataJson.put("currency_code", pgVerifyCallBackReq.getCurrency_code());
        dataJson.put("balance_amount", balance);
        dataJson.put("updated_time", System.currentTimeMillis());
        pgCallBackRespFail.setData(dataJson);
        pgCallBackRespFail.setError(null);
        return pgCallBackRespFail;
    }

    @Override
    public Object pgAdjustmentCallback(PgVerifyCallBackReq pgVerifyCallBackReq, String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.PG_PLATFORM_CODE);
        GamePlatform gamePlatform;
        if(OpenAPIProperties.PG_IS_PLATFORM_LOGIN.equals("Y")) {
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(), gameParentPlatform.getPlatformCode());
        }else {
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(String.valueOf(pgVerifyCallBackReq.getGame_id()), gameParentPlatform.getPlatformCode());
        }
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(pgVerifyCallBackReq.getPlayer_name());
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal money = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, pgVerifyCallBackReq.getAdjustment_transaction_id());
        wrapper.eq(Txns::getPlatform,gameParentPlatform.getPlatformCode());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        PgCallBackResponse pgCallBackRespFail = new PgCallBackResponse();
        JSONObject dataJson = new JSONObject();
        JSONObject errorJson = new JSONObject();

        BigDecimal realWinAmount = pgVerifyCallBackReq.getTransfer_amount();
        if (realWinAmount.compareTo(BigDecimal.ZERO) != 0) {
            balance = balance.add(realWinAmount);
            if (realWinAmount.compareTo(BigDecimal.ZERO) == 1) {//赢
                gameCommonService.updateUserBalance(memBaseinfo, realWinAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }else {
                gameCommonService.updateUserBalance(memBaseinfo, realWinAmount.abs(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
            }
        }
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);

        Txns txns = new Txns();
        if(null!=oldTxns) {
            BeanUtils.copyProperties(oldTxns, txns);
            oldTxns.setStatus("Settle");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);
        }else {
            //游戏商注单号
            txns.setPlatformTxId(pgVerifyCallBackReq.getBet_id());
            //此交易是否是投注 true是投注 false 否
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
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

        }
        //游戏名称
        txns.setGameName(gamePlatform.getPlatformEnName());
        //下注金额
        txns.setBetAmount(realWinAmount);
        //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
        txns.setWinningAmount(realWinAmount);
        //玩家下注时间
        txns.setBetTime(DateUtils.formatByLong(pgVerifyCallBackReq.getCreate_time(), DateUtils.newFormat));
        txns.setBalance(balance);
        txns.setId(null);
        txns.setStatus("Running");
        txns.setRealWinAmount(realWinAmount);//真实返还金额
        txns.setMethod("Settle");
        txns.setCreateTime(dateStr);
        txnsMapper.insert(txns);



        dataJson.put("adjust_amount", money);
        dataJson.put("balance_before", balance);
        dataJson.put("balance_after", balance);
        dataJson.put("updated_time", System.currentTimeMillis());
        pgCallBackRespFail.setData(dataJson);
        pgCallBackRespFail.setError(null);
        return pgCallBackRespFail;
    }


    private boolean checkIp(String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.PG_PLATFORM_CODE);
        if (null == gameParentPlatform) {
            return false;
        } else if (null == gameParentPlatform.getIpAddr() || "".equals(gameParentPlatform.getIpAddr())) {
            return true;
        } else if (gameParentPlatform.getIpAddr().equals(ip)) {
            return true;
        }
        return false;
    }

    @Override
    public Object pgVerifyCallback(PgVerifyCallBackReq pgVerifyCallBackReq, String ip) {
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(pgVerifyCallBackReq.getOperator_player_session(), OpenAPIProperties.PG_PLATFORM_CODE);
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.PG_PLATFORM_CODE);
        PgCallBackResponse pgCallBackRespFail = new PgCallBackResponse();
        JSONObject dataJson = new JSONObject();
        JSONObject errorJson = new JSONObject();
        if (cptOpenMember == null) {
            pgCallBackRespFail.setData(dataJson);
            errorJson.put("code", "1034");
            errorJson.put("message", "无效请求");
            pgCallBackRespFail.setError(errorJson);
            return pgCallBackRespFail;
        }
        dataJson.put("player_name", cptOpenMember.getUserName());
        dataJson.put("nickname", cptOpenMember.getUserName());
        dataJson.put("currency", gameParentPlatform.getCurrencyType());
        pgCallBackRespFail.setData(dataJson);
        pgCallBackRespFail.setError(null);
        return pgCallBackRespFail;
    }


}

