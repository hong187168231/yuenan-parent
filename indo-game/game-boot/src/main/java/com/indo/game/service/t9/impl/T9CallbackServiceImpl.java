package com.indo.game.service.t9.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.t9.T9ApiPayResponseData;
import com.indo.game.pojo.dto.t9.T9ApiPlayerResponseData;
import com.indo.game.pojo.dto.t9.T9ApiResponseData;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.t9.T9CallbackService;
import com.indo.user.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


/**
 * T9 回调服务业务实现
 */
@Service
public class T9CallbackServiceImpl implements T9CallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GameCommonService gameCommonService;

    @Autowired
    private TxnsMapper txnsMapper;

    // 查询余额
    @Override
    public Object queryPoint(String callBackParam, String ip) {
        // 校验IP
        if (!checkIp(ip)) {
            T9ApiResponseData callBackFail = new T9ApiResponseData();
            callBackFail.setStatusCode("70006");
            callBackFail.setErrorMessage("No authorized to access.");
            return JSONObject.toJSONString(callBackFail);
        }

        JSONObject jsonObject = JSONObject.parseObject(callBackParam);
        String playerID = jsonObject.getString("playerID");
        // 查询玩家是否存在
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
        if (null == memBaseinfo) {
            T9ApiResponseData callBackFail = new T9ApiResponseData();
            callBackFail.setStatusCode("70002");
            callBackFail.setErrorMessage("会员不存在");
            return JSONObject.toJSONString(callBackFail);
        }

        // 会员余额返回
        if (memBaseinfo.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            T9ApiResponseData getBalanceSuccess = new T9ApiResponseData();
            getBalanceSuccess.setStatusCode("200");
            T9ApiPlayerResponseData t9ApiPlayerResponseData = new T9ApiPlayerResponseData();
            t9ApiPlayerResponseData.setBalance(memBaseinfo.getBalance());
            getBalanceSuccess.setData(t9ApiPlayerResponseData);
            return JSONObject.toJSONString(getBalanceSuccess);
        }

        // 余额不足 70004
        T9ApiResponseData callBackFail = new T9ApiResponseData();
        callBackFail.setStatusCode("70004");
        callBackFail.setErrorMessage("会员余额不足");
        return JSONObject.toJSONString(callBackFail);
    }

    // 提取点数
    @Override
    public Object withdrawal(String callBackParam, String ip) {

        // 校验IP
        if (!checkIp(ip)) {
            T9ApiResponseData callBackFail = new T9ApiResponseData();
            callBackFail.setStatusCode("70006");
            callBackFail.setErrorMessage("No authorized to access.");
            return JSONObject.toJSONString(callBackFail);
        }
        JSONObject jsonObject = JSONObject.parseObject(callBackParam);
        String paySerialno = jsonObject.getString("paySerialno");   // 交易序号
        String playerID = jsonObject.getString("playerID");  // 玩家账号
        BigDecimal pointAmount = jsonObject.getBigDecimal("pointAmount");  // 提取金额

        // 查询玩家是否存在
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
        if (null == memBaseinfo) {
            T9ApiResponseData callBackFail = new T9ApiResponseData();
            callBackFail.setStatusCode("70002");
            callBackFail.setErrorMessage("会员不存在");
            return JSONObject.toJSONString(callBackFail);
        }

        try {
            BigDecimal balance = memBaseinfo.getBalance();
            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, pointAmount, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);

            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(OpenAPIProperties.T9_PLATFORM_CODE);
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(gamePlatform.getParentName());

            // 查询订单
            Txns oldTxns = getTxns(gameParentPlatform, paySerialno, playerID);
            if (null != oldTxns) {
                T9ApiResponseData callBackFail = new T9ApiResponseData();
                callBackFail.setStatusCode("70008");
                callBackFail.setErrorMessage("游戏平台交易序号(paySerialno)已存在");
                return JSONObject.toJSONString(callBackFail);
            }

            // 生成订单数据
            Txns txns = getInitTxns(gamePlatform, gameParentPlatform, paySerialno, playerID, pointAmount, balance, ip, "Place Bet");
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                T9ApiResponseData callBackFail = new T9ApiResponseData();
                callBackFail.setStatusCode("70004");
                callBackFail.setErrorMessage("会员余额不足");
                return JSONObject.toJSONString(callBackFail);
            }
            T9ApiResponseData callBackSuc = new T9ApiResponseData();
            callBackSuc.setStatusCode("200");
            T9ApiPayResponseData t9ApiPayResponseData = new T9ApiPayResponseData();
            t9ApiPayResponseData.setTransferID(paySerialno);
            t9ApiPayResponseData.setPointAmount(pointAmount);
            callBackSuc.setData(t9ApiPayResponseData);
            return JSONObject.toJSONString(callBackSuc);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        T9ApiResponseData callBackFail = new T9ApiResponseData();
        callBackFail.setStatusCode("70006");
        callBackFail.setErrorMessage("资料库异常");
        return JSONObject.toJSONString(callBackFail);
    }

    // 存入点数
    @Override
    public Object deposit(String callBackParam, String ip) {
        // 校验IP
        if (!checkIp(ip)) {
            T9ApiResponseData callBackFail = new T9ApiResponseData();
            callBackFail.setStatusCode("70006");
            callBackFail.setErrorMessage("No authorized to access.");
            return JSONObject.toJSONString(callBackFail);
        }

        JSONObject jsonObject = JSONObject.parseObject(callBackParam);
        String paySerialno = jsonObject.getString("paySerialno");   // 交易序号
        String playerID = jsonObject.getString("playerID");  // 玩家账号
        BigDecimal pointAmount = jsonObject.getBigDecimal("pointAmount");  // 提取金额

        // 查询玩家是否存在
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
        if (null == memBaseinfo) {
            T9ApiResponseData callBackFail = new T9ApiResponseData();
            callBackFail.setStatusCode("70002");
            callBackFail.setErrorMessage("会员不存在");
            return JSONObject.toJSONString(callBackFail);
        }
        try {
            BigDecimal balance = memBaseinfo.getBalance();
            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, pointAmount, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);

            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(OpenAPIProperties.T9_PLATFORM_CODE);
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(gamePlatform.getParentName());

            // 查询订单
            Txns oldTxns = getTxns(gameParentPlatform, paySerialno, playerID);
            if (null != oldTxns) {
                T9ApiResponseData callBackFail = new T9ApiResponseData();
                callBackFail.setStatusCode("70008");
                callBackFail.setErrorMessage("游戏平台交易序号(paySerialno)已存在");
                return JSONObject.toJSONString(callBackFail);
            }

            // 生成订单数据
            Txns txns = getInitTxns(gamePlatform, gameParentPlatform, paySerialno, playerID, pointAmount, balance, ip, "Settle");
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                T9ApiResponseData callBackFail = new T9ApiResponseData();
                callBackFail.setStatusCode("70006");
                callBackFail.setErrorMessage("资料库异常");
                return JSONObject.toJSONString(callBackFail);
            }
            T9ApiResponseData callBackSuc = new T9ApiResponseData();
            callBackSuc.setStatusCode("200");
            T9ApiPayResponseData t9ApiPayResponseData = new T9ApiPayResponseData();
            t9ApiPayResponseData.setTransferID(paySerialno);
            t9ApiPayResponseData.setPointAmount(pointAmount);
            callBackSuc.setData(t9ApiPayResponseData);
            return JSONObject.toJSONString(callBackSuc);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        T9ApiResponseData callBackFail = new T9ApiResponseData();
        callBackFail.setStatusCode("70006");
        callBackFail.setErrorMessage("资料库异常");
        return JSONObject.toJSONString(callBackFail);
    }

    // 取消交易
    @Override
    public Object canceltransfer(String callBackParam, String ip) {
        // 校验IP
        if (!checkIp(ip)) {
            T9ApiResponseData callBackFail = new T9ApiResponseData();
            callBackFail.setStatusCode("70006");
            callBackFail.setErrorMessage("No authorized to access.");
            return JSONObject.toJSONString(callBackFail);
        }

        JSONObject jsonObject = JSONObject.parseObject(callBackParam);
        String paySerialno = jsonObject.getString("paySerialno");   // 交易序号
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(OpenAPIProperties.T9_PLATFORM_CODE);
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(gamePlatform.getParentName());

        // 查询订单
        Txns oldTxns = getTxns(gameParentPlatform, paySerialno, null);
        if (null == oldTxns) {
            T9ApiResponseData callBackFail = new T9ApiResponseData();
            callBackFail.setStatusCode("70009");
            callBackFail.setErrorMessage("无法取消不存在的游戏平台交易序号(paySerialno)");
            return JSONObject.toJSONString(callBackFail);
        }

        // 查询玩家是否存在
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(oldTxns.getUserId());
        if (null == memBaseinfo) {
            T9ApiResponseData callBackFail = new T9ApiResponseData();
            callBackFail.setStatusCode("70002");
            callBackFail.setErrorMessage("会员不存在");
            return JSONObject.toJSONString(callBackFail);
        }

        BigDecimal balance = memBaseinfo.getBalance();

        try{
            if (!"Cancel Bet".equals(oldTxns.getMethod())) {
                BigDecimal betAmount = oldTxns.getBetAmount();
                // 存入点数失败, 余额扣除
                if("Settle".equals(oldTxns.getMethod())){

                    balance = balance.subtract(betAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                } else if("Place Bet".equals(oldTxns.getMethod())){ // 提取点数失败, 余额加上

                    balance = balance.add(betAmount.abs());
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
                }

                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setId(null);
                txns.setBalance(balance);
                txns.setMethod("Cancel Bet");
                txns.setStatus("Running");
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
                oldTxns.setStatus("Cancel");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);
            }

        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            T9ApiResponseData callBackFail = new T9ApiResponseData();
            callBackFail.setStatusCode("70006");
            callBackFail.setErrorMessage("资料库异常");
            return JSONObject.toJSONString(callBackFail);
        }

        T9ApiResponseData callBackSuc = new T9ApiResponseData();
        callBackSuc.setStatusCode("200");
        return JSONObject.toJSONString(callBackSuc);
    }

    /**
     * 查询第三方订单是否存在
     *
     * @param gameParentPlatform
     * @param paySerialno
     * @param playerID
     * @return
     */
    private Txns getTxns(GameParentPlatform gameParentPlatform, String paySerialno, String playerID) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        if (StringUtils.isNoneBlank(playerID)) {
            wrapper.eq(Txns::getUserId, playerID);
        }
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 初始化第三方游戏交互订单数据
     *
     * @param gamePlatform
     * @param gameParentPlatform
     * @param paySerialno
     * @param playerID
     * @param pointAmount
     * @param balance
     * @param ip
     * @param method
     * @return
     */
    private Txns getInitTxns(GamePlatform gamePlatform, GameParentPlatform gameParentPlatform,
                             String paySerialno, String playerID, BigDecimal pointAmount, BigDecimal balance, String ip, String method) {
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(paySerialno);
        //此交易是否是投注 true是投注 false 否
        txns.setBet(false);
        //玩家 ID
        txns.setUserId(playerID);
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
        //下注金额
        txns.setBetAmount(pointAmount);
        //创建时间
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        //玩家下注时间
        txns.setBetTime(dateStr);
        //有效投注金额 或 投注面值
        txns.setTurnover(pointAmount);
        //辨认交易时间依据
        txns.setTxTime(dateStr);
        //操作名称
        txns.setMethod(method);
        txns.setStatus("Running");
        //余额
        txns.setBalance(balance);
        txns.setCreateTime(dateStr);
        //投注 IP
        txns.setBetIp(ip);
        return txns;
    }

    /**
     * 查询IP是否被封
     * @param ip
     * @return
     */
    private boolean checkIp(String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode("JDB");
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
