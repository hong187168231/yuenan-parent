package com.indo.game.service.t9.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.t9.T9CallbackService;
import com.indo.core.pojo.bo.MemTradingBO;
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
        logger.info("t9_queryPoint t9Game paramJson:{}, ip:{}", callBackParam, ip);
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(70006, "No authorized to access.");
        }

        JSONObject jsonObject = JSONObject.parseObject(callBackParam);
        logger.info("t9_queryPoint t9Game paramJson:{}, ip:{}", JSONObject.toJSONString(jsonObject), ip);
        String playerID = jsonObject.getString("playerID");
        // 查询玩家是否存在
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
        if (null == memBaseinfo) {
            return initFailureResponse(70002, "会员不存在");
        }

        // 会员余额返回
        if (memBaseinfo.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            JSONObject jsonObject1 = initSuccessResponse();
            JSONObject balance = new JSONObject();
            balance.put("balance", memBaseinfo.getBalance());
            jsonObject1.put("data", balance);
            return jsonObject1;
        }

        // 余额不足 70004
        return initFailureResponse(70004, "会员余额不足");
    }

    // 提取点数
    @Override
    public Object withdrawal(String callBackParam, String ip) {

        GameParentPlatform platformGameParent = getGameParentPlatform();

        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(70006, "No authorized to access.");
        }
        JSONObject jsonObject = JSONObject.parseObject(callBackParam);
        logger.info("t9_withdrawal t9Game paramJson:{}, ip:{}", JSONObject.toJSONString(jsonObject), ip);
        String paySerialno = jsonObject.getString("paySerialno");   // 交易序号
        String playerID = jsonObject.getString("playerID");  // 玩家账号
        BigDecimal pointAmount = jsonObject.getBigDecimal("pointAmount");  // 提取金额

        // 查询玩家是否存在
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
        if (null == memBaseinfo) {
            return initFailureResponse(70002, "会员不存在");
        }

        try {
            BigDecimal balance = memBaseinfo.getBalance();

            if (memBaseinfo.getBalance().compareTo(pointAmount) < 0) {
                return initFailureResponse(1, "玩家余额不足");
            }

            // 查询订单
            Txns oldTxns = getTxns(platformGameParent, paySerialno, playerID);
            if (null != oldTxns) {
                return initFailureResponse(70008, "游戏平台交易序号(paySerialno)已存在");
            }
            balance = balance.subtract(pointAmount);
            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, pointAmount, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);

            // 生成订单数据
            Txns txns = getInitTxns(platformGameParent, paySerialno, playerID, pointAmount, balance, ip, "Place Bet");
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(70004, "会员余额不足");
            }

            JSONObject jsonObject1 = initSuccessResponse();
            JSONObject dataJson = new JSONObject();
            dataJson.put("transferID", paySerialno);
            dataJson.put("pointAmount", pointAmount);
            jsonObject1.put("data", dataJson);
            return jsonObject1;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return initFailureResponse(70006, "资料库异常");
    }

    // 存入点数
    @Override
    public Object deposit(String callBackParam, String ip) {

        GameParentPlatform platformGameParent = getGameParentPlatform();

        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(70006, "No authorized to access.");
        }

        JSONObject jsonObject = JSONObject.parseObject(callBackParam);
        logger.info("t9_deposit t9Game paramJson:{}, ip:{}", JSONObject.toJSONString(jsonObject), ip);
        String paySerialno = jsonObject.getString("paySerialno");   // 交易序号
        String playerID = jsonObject.getString("playerID");  // 玩家账号
        BigDecimal pointAmount = jsonObject.getBigDecimal("pointAmount");  // 提取金额

        // 查询玩家是否存在
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
        if (null == memBaseinfo) {
            return initFailureResponse(70002, "会员不存在");
        }
        try {
            BigDecimal balance = memBaseinfo.getBalance();
            balance = balance.add(pointAmount);
            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, pointAmount, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);

            // 查询订单
            Txns oldTxns = getTxns(platformGameParent, paySerialno, playerID);
            if (null != oldTxns) {
                return initFailureResponse(70008, "游戏平台交易序号(paySerialno)已存在");
            }

            // 生成订单数据
            Txns txns = getInitTxns(platformGameParent, paySerialno, playerID, pointAmount, balance, ip, "Settle");
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(70006, "资料库异常");
            }

            JSONObject jsonObject1 = initSuccessResponse();
            JSONObject dataJson = new JSONObject();
            dataJson.put("transferID", paySerialno);
            dataJson.put("pointAmount", pointAmount);
            jsonObject1.put("data", dataJson);
            return jsonObject1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return initFailureResponse(70006, "资料库异常");
    }

    // 取消交易
    @Override
    public Object canceltransfer(String callBackParam, String ip) {
        logger.info("t9_canceltransfer t9Game paramJson:{}, ip:{}", callBackParam, ip);
        GameParentPlatform platformGameParent = getGameParentPlatform();

        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(70006, "No authorized to access.");
        }

        JSONObject jsonObject = JSONObject.parseObject(callBackParam);
        logger.info("t9_canceltransfer t9Game paramJson:{}, ip:{}", JSONObject.toJSONString(jsonObject), ip);
        String paySerialno = jsonObject.getString("paySerialno");   // 交易序号

        // 查询订单
        Txns oldTxns = getTxns(platformGameParent, paySerialno, null);
        if (null == oldTxns) {
            return initFailureResponse(70009, "无法取消不存在的游戏平台交易序号(paySerialno)");
        }

        // 查询玩家是否存在
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(oldTxns.getUserId());
        if (null == memBaseinfo) {
            return initFailureResponse(70002, "会员不存在");
        }

        BigDecimal balance = memBaseinfo.getBalance();

        try {
            if (!"Cancel Bet".equals(oldTxns.getMethod())) {
                BigDecimal betAmount = oldTxns.getBetAmount();

                // 存入点数失败, 余额扣除
                if ("Settle".equals(oldTxns.getMethod())) {
                    // 会员余额不足
                    if (balance.compareTo(betAmount) < 0) {
                        return initFailureResponse(70004, "会员余额不足");
                    }
                    balance = balance.subtract(betAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                } else if ("Place Bet".equals(oldTxns.getMethod())) { // 提取点数失败, 余额加上

                    balance = balance.add(betAmount.abs());
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
                }

                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setId(null);
                txns.setPlatformTxId(GoldchangeEnum.CANCEL_BET.name() + GeneratorIdUtil.generateId());
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

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(70006, "资料库异常");
        }

        return initSuccessResponse();
    }

    /**
     * 查询第三方订单是否存在
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @param playerID           playerID
     * @return Txns
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
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @param playerID           playerID
     * @param pointAmount        pointAmount
     * @param balance            balance
     * @param ip                 ip
     * @param method             method
     * @return Txns
     */
    private Txns getInitTxns(GameParentPlatform gameParentPlatform, String paySerialno, String playerID,
                             BigDecimal pointAmount, BigDecimal balance, String ip, String method) {
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.T9_PLATFORM_CODE).get(0);
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
        if("Place Bet".equals(method)){
            txns.setWinningAmount(pointAmount.negate());
        }else {
            txns.setWinningAmount(pointAmount);
        }
        txns.setWinAmount(pointAmount);
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
     *
     * @param ip ip
     * @return boolean
     */
    private boolean checkIp(String ip, GameParentPlatform platformGameParent) {
        if (null == platformGameParent) {
            return true;
        } else if (null == platformGameParent.getIpAddr() || "".equals(platformGameParent.getIpAddr())) {
            return false;
        }
        return !platformGameParent.getIpAddr().equals(ip);

    }

    /**
     * 初始化成功json返回
     *
     * @return JSONObject
     */
    private JSONObject initSuccessResponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("statusCode", 200);
        jsonObject.put("errorMessage", "Success");
        return jsonObject;
    }

    /**
     * 初始化交互失败返回
     *
     * @param error       错误码
     * @param description 错误描述
     * @return JSONObject
     */
    private JSONObject initFailureResponse(Integer error, String description) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("statusCode", error);
        jsonObject.put("errorMessage", description);
        return jsonObject;
    }

    private GameParentPlatform getGameParentPlatform(){
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.T9_PLATFORM_CODE);
    }
}
