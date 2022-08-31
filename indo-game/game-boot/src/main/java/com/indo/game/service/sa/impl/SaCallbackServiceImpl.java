package com.indo.game.service.sa.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.game.common.util.XmlUtil;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.sa.SaCallbackResp;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.sa.SaCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class SaCallbackServiceImpl implements SaCallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object getUserBalance(String params, String ip) {
        logger.info("sa_getBalance saGame paramJson:{}, ip:{}", params, ip);
        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(1005, "非信任來源IP");
            }

            JSONObject jsonObject = convertParam(params);
            if (null == jsonObject) {
                return initFailureResponse(1006, "解密错误");
            }

            if (!jsonObject.containsKey("username") || StringUtils.isBlank(jsonObject.getString("username"))) {
                return initFailureResponse(1000, "帐号名不得为空");
            }

            if (!jsonObject.containsKey("currency")
                    || StringUtils.isBlank(jsonObject.getString("currency"))
                    || !gameParentPlatform.getCurrencyType().equals(jsonObject.getString("currency"))) {
                return initFailureResponse(1001, "货币代码不正确");
            }

            String account = jsonObject.getString("username");
            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
            if (null == memBaseinfo) {
                return initFailureResponse(1000, "查无此帐号,请检查");
            }

            return initSuccessResponse(account, gameParentPlatform.getCurrencyType(), memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(9999, e.getMessage());
        }
    }

    // 下注
    @Override
    public Object placeBet(String params, String ip) {
        logger.info("sa_placeBet saGame paramJson:{}, ip:{}", params, ip);
        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(1005, "非信任來源IP");
            }

            JSONObject jsonObject = convertParam(params);
            if (null == jsonObject) {
                return initFailureResponse(1006, "解密错误");
            }

            if (!jsonObject.containsKey("username") || StringUtils.isBlank(jsonObject.getString("username"))) {
                return initFailureResponse(1000, "帐号名不得为空");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(jsonObject.getString("username"));
            if (null == memBaseinfo) {
                return initFailureResponse(1000, "查无此帐号,请检查");
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 下注金额
            BigDecimal betAmount = null!=jsonObject.getBigDecimal("amount")?jsonObject.getBigDecimal("amount").multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
            // 下注金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                return initFailureResponse(1002, "下注金额不能小0");
            }

            if (balance.compareTo(betAmount) < 0) {
                return initFailureResponse(1004, "玩家没有足够余额以进行当前下注");
            }
            balance = balance.subtract(betAmount);

            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount,
                    GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

            String gameId = jsonObject.getString("gameid");
            String txnId = jsonObject.getString("txnid");
            // 查询用户请求订单
            Txns oldTxns = getTxnsByRounId(gameParentPlatform, gameId);
            if (null != oldTxns) {
                return initFailureResponse(122, "交易已存在");
            }

            Date timestamp = jsonObject.getDate("timestamp");
            String gametype = jsonObject.getString("gametype");

            GamePlatform gamePlatform;
            if(OpenAPIProperties.SA_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.SA_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
            }else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gametype,gameParentPlatform.getPlatformCode());
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(txnId);

            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(gameParentPlatform.getCurrencyType());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
            txns.setRoundId(gameId);
            txns.setGameInfo(jsonObject.getString("betdetails"));
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
            txns.setBetAmount(betAmount);
            //游戏平台的下注项目
            txns.setBetType(gametype);
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount.negate());
            //玩家下注时间
            txns.setBetTime(DateUtils.format(timestamp, DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(betAmount);
            //真实返还金额,游戏赢分
//            txns.setRealWinAmount(freeWinAmount);
            //返还金额 (包含下注金额)
            txns.setWinAmount(betAmount);
            //有效投注金额 或 投注面值
            txns.setTurnover(betAmount);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.format(timestamp, DateUtils.newFormat));
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
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(9999, "订单写入失败");
            }

            return initSuccessResponse(memBaseinfo.getAccount(), gameParentPlatform.getCurrencyType(), memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(9999, e.getMessage());
        }
    }

    // 结算注单,更新余额中奖
    @Override
    public Object playerWin(String params, String ip) {
        logger.info("sa_playerWin saGame paramJson:{}, ip:{}", params, ip);
        try {

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(1005, "非信任來源IP");
            }

            JSONObject jsonObject = convertParam(params);
            if (null == jsonObject) {
                return initFailureResponse(1006, "解密错误");
            }

            if (!jsonObject.containsKey("username") || StringUtils.isBlank(jsonObject.getString("username"))) {
                return initFailureResponse(1000, "帐号名不得为空");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(jsonObject.getString("username"));
            if (null == memBaseinfo) {
                return initFailureResponse(1000, "查无此帐号,请检查");
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 派奖金额
            BigDecimal amount = null!=jsonObject.getBigDecimal("amount")?jsonObject.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            // 派彩金额小于0
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                return initFailureResponse(1005, "派彩金额不能小0");
            }

            String gameid = jsonObject.getString("gameid");
            // 查询用户请求订单
            Txns oldTxns = getTxnsByRounId(platformGameParent, gameid);
            if (null == oldTxns) {
                return initFailureResponse(152, "交易不存在");
            }

            balance = balance.add(amount);
            gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            String txnId = jsonObject.getString("txnid");
            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep = 0;
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(amount);
            txns.setResultType(resultTyep);

            //真实返还金额,游戏赢分
            txns.setRealWinAmount(amount);
            txns.setPlatformTxId(txnId);
            //返还金额 (包含下注金额)
            txns.setWinAmount(amount);

            txns.setBalance(balance);
            txns.setMethod("Settle");
            txns.setStatus("Running");
            oldTxns.setStatus("Settle");
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            txns.setCreateTime(dateStr);
            oldTxns.setUpdateTime(DateUtils.format(jsonObject.getDate("timestamp"), DateUtils.newFormat));
            int num = txnsMapper.updateById(oldTxns);
            if (num <= 0) {
                return initFailureResponse(1, "订单写入失败");
            }
            txnsMapper.insert(txns);
            return initSuccessResponse(memBaseinfo.getAccount(), platformGameParent.getCurrencyType(), balance.divide(platformGameParent.getCurrencyPro()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(9999, e.getMessage());
        }
    }

    // 结算注单,不更新余额
    @Override
    public Object playerLost(String params, String ip) {
        logger.info("sa_credit saGame paramJson:{}, ip:{}", params, ip);
        try {

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(1005, "非信任來源IP");
            }

            JSONObject jsonObject = convertParam(params);
            if (null == jsonObject) {
                return initFailureResponse(1006, "解密错误");
            }

            if (!jsonObject.containsKey("username") || StringUtils.isBlank(jsonObject.getString("username"))) {
                return initFailureResponse(1000, "帐号名不得为空");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(jsonObject.getString("username"));
            if (null == memBaseinfo) {
                return initFailureResponse(1000, "查无此帐号,请检查");
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            String gameid = jsonObject.getString("gameid");
            String txnId = jsonObject.getString("txnid");
            // 查询用户请求订单
            Txns oldTxns = getTxnsByRounId(platformGameParent, gameid);
            if (null == oldTxns) {
                return initFailureResponse(152, "交易不存在");
            }
            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            txns.setPlatformTxId(txnId);
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep = 1;
            oldTxns.setResultType(resultTyep);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(BigDecimal.ZERO);
            //返还金额 (包含下注金额)
            txns.setWinAmount(BigDecimal.ZERO);
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(oldTxns.getBetAmount().negate());
            txns.setBalance(balance);
            txns.setMethod("Settle");
            txns.setStatus("Running");
            oldTxns.setStatus("Settle");
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            txns.setCreateTime(dateStr);
            oldTxns.setUpdateTime(DateUtils.format(jsonObject.getDate("timestamp"), DateUtils.newFormat));
            int num = txnsMapper.updateById(oldTxns);
            if (num <= 0) {
                return initFailureResponse(1, "订单写入失败");
            }
            txnsMapper.insert(txns);
            return initSuccessResponse(memBaseinfo.getAccount(), platformGameParent.getCurrencyType(), balance.divide(platformGameParent.getCurrencyPro()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(9999, e.getMessage());
        }
    }

    // 取消注单
    @Override
    public Object placeBetCancel(String params, String ip) {
        logger.info("sa_revoke saGame paramJson:{}, ip:{}", params, ip);
        try {

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(1005, "非信任來源IP");
            }

            JSONObject jsonObject = convertParam(params);
            if (null == jsonObject) {
                return initFailureResponse(1006, "解密错误");
            }

            if (!jsonObject.containsKey("username") || StringUtils.isBlank(jsonObject.getString("username"))) {
                return initFailureResponse(1000, "帐号名不得为空");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(jsonObject.getString("username"));
            if (null == memBaseinfo) {
                return initFailureResponse(1000, "查无此帐号,请检查");
            }

            String txnid = jsonObject.getString("txnid");
            // 查询用户请求订单
            Txns oldTxns = getTxns(platformGameParent, txnid);
            if (null == oldTxns) {
                return initSuccessResponse(memBaseinfo.getAccount(), platformGameParent.getCurrencyType(), memBaseinfo.getBalance());
            }

            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getStatus())) {
                return initSuccessResponse(memBaseinfo.getAccount(), platformGameParent.getCurrencyType(), memBaseinfo.getBalance());
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance().add(oldTxns.getBetAmount());
            gameCommonService.updateUserBalance(memBaseinfo, memBaseinfo.getBalance(), GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);

            //返还金额 (包含下注金额)
            txns.setBalance(balance);
            txns.setMethod("Cancel Bet");
            txns.setStatus("Running");
            oldTxns.setStatus("Cancel Bet");
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            txns.setCreateTime(dateStr);
            oldTxns.setUpdateTime(DateUtils.format(jsonObject.getDate("timestamp"), DateUtils.newFormat));
            int num = txnsMapper.updateById(oldTxns);
            if (num <= 0) {
                return initFailureResponse(1, "取消注单订单写入失败");
            }
            txnsMapper.insert(txns);

            return initSuccessResponse(memBaseinfo.getAccount(), platformGameParent.getCurrencyType(), balance.divide(platformGameParent.getCurrencyPro()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(9999, e.getMessage());
        }
    }

    /**
     * 转换参数类型
     *
     * @param params 参数 username=satest&currency=EUR
     * @return JSONObject
     */
    private JSONObject convertParam(String params) {
        JSONObject jsonObject = null;
        if (StringUtils.isNotEmpty(params)) {
            jsonObject = new JSONObject();
            String[] paramList = params.split("&");
            for (String param : paramList) {
                String[] paramInfos = param.split("=");
                jsonObject.put(paramInfos[0], paramInfos[1]);
            }
        }
        return jsonObject;
    }

    /**
     * 查询第三方订单是否存在
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @return Txns
     */
    private Txns getTxns(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        return txnsMapper.selectOne(wrapper);
    }

    private Txns getTxnsByRounId(GameParentPlatform gameParentPlatform, String rounId) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getRoundId, rounId);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        return txnsMapper.selectOne(wrapper);
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
    private Object initSuccessResponse(String username, String currency, BigDecimal amount) {
        SaCallbackResp saLoginResp = new SaCallbackResp();
        saLoginResp.setError(0);
        saLoginResp.setUsername(username);
        saLoginResp.setAmount(amount);
        saLoginResp.setCurrency(currency);
        return XmlUtil.convertToXml(saLoginResp, "UTF-8", false);
//        return saLoginResp;
    }

    /**
     * 初始化交互失败返回
     *
     * @param error       错误码
     * @param description 错误描述
     * @return JSONObject
     */
    private Object initFailureResponse(Integer error, String description) {
        SaCallbackResp saLoginResp = new SaCallbackResp();
        saLoginResp.setError(error);
        logger.info("initFailureResponse error:{}, description:{}",error, description);
        return XmlUtil.convertToXml(saLoginResp, "UTF-8", false);
//        return saLoginResp;
    }

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SA_PLATFORM_CODE);
    }
}
