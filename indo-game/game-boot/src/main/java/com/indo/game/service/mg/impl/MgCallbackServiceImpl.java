package com.indo.game.service.mg.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.mg.MgCallBackReq;
import com.indo.game.pojo.dto.pg.PgVerifyCallBackReq;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.vo.callback.pg.PgCallBackResponse;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.mg.MgCallbackService;
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
 * MG
 *
 * @author
 */
@Service
public class MgCallbackServiceImpl implements MgCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object mgBalanceCallback(MgCallBackReq mgCallBackReq, String ip) {
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MG_PLATFORM_CODE);
        //进行秘钥
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(mgCallBackReq.getPlayerId());
        JSONObject dataJson = new JSONObject();
        if (null == memBaseinfo) {
            dataJson.put("code", "401");
            dataJson.put("message", "API 令牌過期或無效");
            return dataJson;
        } else {
            dataJson.put("balance", memBaseinfo.getBalance());
            dataJson.put("currency", platformGameParent.getCurrencyType());
            return dataJson;
        }
    }


    private boolean checkIp(String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MG_PLATFORM_CODE);
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
    public Object mgVerifyCallback(MgCallBackReq mgCallBackReq, String ip) {
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(mgCallBackReq.getPlayerId());
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MG_PLATFORM_CODE);
        JSONObject dataJson = new JSONObject();
        if (memBaseinfo == null) {
            dataJson.put("code", "401");
            dataJson.put("message", "API 令牌過期或無效");
            return dataJson;
        }
        dataJson.put("balance", memBaseinfo.getBalance());
        dataJson.put("currency", platformGameParent.getCurrencyType());
        return dataJson;
    }



    @Override
    public Object mgUpdatebalanceCallback(MgCallBackReq mgCallBackReq, String ip) {
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(mgCallBackReq.getPlayerId());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MG_PLATFORM_CODE);
        GamePlatform gamePlatform ;
        if(OpenAPIProperties.MG_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.MG_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
        }else {
            gamePlatform = gameCommonService.getGamePlatformByplatformCode("");
        }
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        PgCallBackResponse pgCallBackRespFail = new PgCallBackResponse();
        JSONObject dataJson = new JSONObject();
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, mgCallBackReq.getTxnId());
        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                dataJson.put("code", "400");
                dataJson.put("message", "无效请求");
                return dataJson;
            } else {
                dataJson.put("code", "1034");
                dataJson.put("message", "无效请求");
                return pgCallBackRespFail;
            }
        }

        if ("CREDIT".equals(mgCallBackReq.getTxnType())) {//赢
            balance = balance.add(mgCallBackReq.getAmount());
            gameCommonService.updateUserBalance(memBaseinfo, mgCallBackReq.getAmount(), GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
        }
        if ("DEBIT".equals(mgCallBackReq.getTxnType())) {//输
            if (memBaseinfo.getBalance().compareTo(mgCallBackReq.getAmount()) == -1) {
                dataJson.put("code", "402");
                dataJson.put("message", "餘額不足");
                return dataJson;
            }
            balance = balance.subtract(mgCallBackReq.getAmount());
            gameCommonService.updateUserBalance(memBaseinfo, mgCallBackReq.getAmount(), GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
        }


        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(mgCallBackReq.getTxnId());
        //此交易是否是投注 true是投注 false 否
        //玩家 ID
        txns.setUserId(memBaseinfo.getAccount());
        //平台代码
        txns.setPlatform(gameParentPlatform.getPlatformCode());
        //平台名称
        txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
        txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
        //平台游戏类型
        txns.setGameType(gameCategory.getGameType());
        //游戏分类ID
        txns.setCategoryId(gameCategory.getId());
        //游戏分类名称
        txns.setCategoryName(gameCategory.getGameName());
        //平台游戏代码
        txns.setGameCode(gamePlatform.getPlatformCode());
        //下注金额
        txns.setBetAmount(mgCallBackReq.getAmount());
        //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
        txns.setWinningAmount(mgCallBackReq.getAmount());
        //玩家下注时间
        txns.setBetTime(DateUtils.formatByString(mgCallBackReq.getCreationTime(), DateUtils.newFormat));
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(mgCallBackReq.getAmount());
        //真实返还金额,游戏赢分
        txns.setRealWinAmount(mgCallBackReq.getAmount());
        //返还金额 (包含下注金额)
        //赌注的结果 : 赢:0,输:1,平手:2
        int resultTyep;
        //有效投注金额 或 投注面值
        txns.setTurnover(mgCallBackReq.getAmount());
        //辨认交易时间依据
        txns.setTxTime(DateUtils.formatByString(mgCallBackReq.getCreationTime(),DateUtils.newFormat));
        //操作名称
        txns.setMethod("Place Bet");
        txns.setStatus("Running");
        //余额
        txns.setBalance(balance);
        //创建时间
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        txns.setCreateTime(dateStr);
        //投注 IP
        txns.setBetIp(ip);//  string 是 投注 IP
        if (oldTxns != null) {
            txns.setStatus("Settle");
            txnsMapper.updateById(oldTxns);
        }
        int num = txnsMapper.insert(txns);
        if (num <= 0) {
            dataJson.put("code", "400");
            dataJson.put("message", "无效请求");
            return dataJson;
        }
        dataJson.put("currency", mgCallBackReq.getCurrency());
        dataJson.put("balance_amount", balance);
        return dataJson;
    }

}

