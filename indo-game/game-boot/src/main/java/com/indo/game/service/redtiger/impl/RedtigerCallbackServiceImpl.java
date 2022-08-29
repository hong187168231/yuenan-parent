package com.indo.game.service.redtiger.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.redtiger.RedtigerCallbackService;
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
public class RedtigerCallbackServiceImpl implements RedtigerCallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;

    @Autowired
    private TxnsMapper txnsMapper;
    @Override
    public Object sid(JSONObject params,String authToken ,String ip) {
        logger.info("redtiger_check redtigerGame paramJson:{}, ip:{}", params, ip);
        JSONObject jsonObject = new JSONObject();
        String uuid = params.getString("uuid");
        String sid = params.getString("sid");
        jsonObject.put("sid",sid);
        jsonObject.put("status", "OK");
        jsonObject.put("uuid", uuid);
        try {


            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                jsonObject.put("status", "INVALID_TOKEN_ID");
                return jsonObject;
            }

            String playerID = params.getString("userId");
            if (null == playerID || "".equals(playerID)) {
                jsonObject.put("status", "TEMPORARY_ERROR");
                return jsonObject;
            }
            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
            if (null == memBaseinfo) {
                jsonObject.put("status", "INVALID_PARAMETER");
                return jsonObject;
            }

            if (null == sid || "".equals(sid)) {
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(playerID, OpenAPIProperties.REDTIGER_PLATFORM_CODE);
                if(null!=cptOpenMember) {
                    sid = cptOpenMember.getPassword();
                }else {
                    sid = uuid;
                }
            }
            jsonObject.put("sid",sid);
            jsonObject.put("uuid", uuid);

            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("status", "INVALID_TOKEN_ID");
            return jsonObject;
        }
    }

    @Override
    public Object check(JSONObject params,String authToken , String ip) {
        logger.info("redtiger_check redtigerGame paramJson:{}, ip:{}", params, ip);
        JSONObject jsonObject = new JSONObject();
        String uuid = params.getString("uuid");
        String sid = params.getString("sid");
        jsonObject.put("sid",sid);
        jsonObject.put("status", "OK");
        jsonObject.put("uuid", uuid);
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                jsonObject.put("status", "INVALID_TOKEN_ID");
                return jsonObject;
            }
            String playerID = params.getString("userId");
            if (null == playerID || "".equals(playerID)) {
                jsonObject.put("status", "TEMPORARY_ERROR");
                return jsonObject;
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
            if (null == memBaseinfo) {
                jsonObject.put("status", "INVALID_PARAMETER");
                return jsonObject;
            }


            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("status", "INVALID_TOKEN_ID");
            return jsonObject;
        }
    }

    @Override
    public Object balance(JSONObject params,String authToken , String ip) {
        logger.info("redtiger_balance redtigerGame paramJson:{}, ip:{}", params, ip);
        JSONObject jsonObject = new JSONObject();
        String uuid = params.getString("uuid");
        jsonObject.put("status", "OK");
        jsonObject.put("uuid", uuid);
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_TOKEN_ID");
                return jsonObject;
            }
            String playerID = params.getString("userId");
            if (null == playerID || "".equals(playerID)) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "TEMPORARY_ERROR");
                return jsonObject;
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
            if (null == memBaseinfo) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_PARAMETER");
                return jsonObject;
            }

            // 会员余额返回
            jsonObject.put("balance", memBaseinfo.getBalance().divide(platformGameParent.getCurrencyPro()));
            jsonObject.put("bonus", 0);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("balance", BigDecimal.ZERO);
            jsonObject.put("bonus", 0);
            jsonObject.put("status", "INVALID_TOKEN_ID");
            return jsonObject;
        }
    }

    @Override
    public Object debit(JSONObject params,String authToken , String ip) {
        logger.info("redtiger_debit  redtigerGame paramJson:{}, ip:{}", params, ip);
        JSONObject jsonObject = new JSONObject();
        String uuid = params.getString("uuid");
        jsonObject.put("status", "OK");
        jsonObject.put("uuid", uuid);
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_TOKEN_ID");
                return jsonObject;
            }

//            JSONObject params = JSONObject.parseObject(String.valueOf(map));
            String playerID = params.getString("userId");
            if (null == playerID || "".equals(playerID)) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "TEMPORARY_ERROR");
                return jsonObject;
            }

            JSONObject transaction = params.getJSONObject("transaction");
            String platformTxId = transaction.getString("id");
            String refId = transaction.getString("refId");
            JSONObject game = params.getJSONObject("game");
            JSONObject table = game.getJSONObject("details").getJSONObject("table");

            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
            if (null == memBaseinfo) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_PARAMETER");
                return jsonObject;
            }
            GamePlatform gamePlatform;
            if("Y".equals(OpenAPIProperties.REDTIGER_IS_PLATFORM_LOGIN)){
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformGameParent.getPlatformCode(),platformGameParent.getPlatformCode());
            }else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(table.getString("id"), platformGameParent.getPlatformCode());

            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 下注金额
            BigDecimal betAmount = null!=transaction.getBigDecimal("amount")?transaction.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            if (memBaseinfo.getBalance().compareTo(betAmount) < 0) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INSUFFICIENT_FUNDS");
                return jsonObject;
            }

            // 查询用户请求订单
            Txns oldTxns = getTxnsByRoundId(refId, memBaseinfo.getAccount());
            if (null != oldTxns&&"Place Bet".equals(oldTxns.getMethod())) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
//                jsonObject.put("status", "BET_ALREADY_SETTLED");
                return jsonObject;
            }else if (null != oldTxns){
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "FINAL_ERROR_ACTION_FAILED");
                return jsonObject;
            }

            // 下注金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INSUFFICIENT_FUNDS");
                return jsonObject;
            }
            balance = balance.subtract(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(platformTxId);
            //此交易是否是投注 true是投注 false 否
            txns.setBet(true);
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
            txns.setGameInfo(game.getString("type"));
            txns.setRoundId(refId);
            //平台代码
            txns.setPlatform(platformGameParent.getPlatformCode());
            //平台名称
            txns.setPlatformEnName(platformGameParent.getPlatformEnName());
            txns.setPlatformCnName(platformGameParent.getPlatformCnName());
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
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount.negate());
            txns.setWinAmount(betAmount);
            //游戏平台的下注项目
            txns.setBetType(table.getString("id"));
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
//            txns.setWinningAmount(BigDecimal.ZERO);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(System.currentTimeMillis(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(betAmount);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(BigDecimal.ZERO);
            //返还金额 (包含下注金额)
            //赌注的结果 : 赢:0,输:1,平手:2
            //有效投注金额 或 投注面值
            txns.setTurnover(betAmount);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(System.currentTimeMillis(), DateUtils.newFormat));
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
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_TOKEN_ID");
                return jsonObject;
            }

            jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
            jsonObject.put("bonus", 0);
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            jsonObject.put("balance", BigDecimal.ZERO);
            jsonObject.put("bonus", 0);
            jsonObject.put("status", "INVALID_TOKEN_ID");
            return jsonObject;
        }
    }

    @Override
    public Object credit(JSONObject params,String authToken , String ip) {
        logger.info("redtiger_credit redtigerGame paramJson:{}, ip:{}", params, ip);
        JSONObject jsonObject = new JSONObject();
        String uuid = params.getString("uuid");
        jsonObject.put("status", "OK");
        jsonObject.put("uuid", uuid);
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_TOKEN_ID");
                return jsonObject;
            }
            String playerID = params.getString("userId");
            if (null == playerID || "".equals(playerID)) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "TEMPORARY_ERROR");
                return jsonObject;
            }

            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
            if (null == memBaseinfo) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_PARAMETER");
                return jsonObject;
            }
            BigDecimal balance = memBaseinfo.getBalance();
            JSONObject transaction = params.getJSONObject("transaction");
            String platformTxId = transaction.getString("id");
            String refId = transaction.getString("refId");
            JSONObject game = params.getJSONObject("game");
//            Txns oldTxns1 = getTxns(platformTxId, memBaseinfo.getAccount());
//            if(null!=oldTxns1){
//                jsonObject.put("balance", balance);
//                jsonObject.put("bonus", 0);
//                jsonObject.put("status", "BET_ALREADY_EXIST");
//                return jsonObject;
//            }
            // 查询用户请求订单
            Txns oldTxns = getTxnsByRoundId(refId, memBaseinfo.getAccount());
            if (null == oldTxns) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "BET_DOES_NOT_EXIST");
                return jsonObject;
            }
            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getMethod())||"Settle".equals(oldTxns.getMethod())) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "BET_ALREADY_SETTLED");
                return jsonObject;
            }
            // 中奖金额
            BigDecimal betAmount = null!=transaction.getBigDecimal("amount")?transaction.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            // 中奖金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INSUFFICIENT_FUNDS");
                return jsonObject;
            }
            if (betAmount.compareTo(BigDecimal.ZERO) != 0) {
                // 会员余额
                balance =balance.add(betAmount);
                // 更新玩家余额
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            //游戏商注单号
            txns.setPlatformTxId(platformTxId);
            txns.setBalance(balance);
            txns.setId(null);
            //更新时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount);
            txns.setWinAmount(betAmount);
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep;
            if (betAmount.compareTo(BigDecimal.ZERO) == 0) {
                resultTyep = 2;
            } else if (betAmount.compareTo(BigDecimal.ZERO) > 0) {
                resultTyep = 0;
            } else {
                resultTyep = 1;
            }
            oldTxns.setResultType(resultTyep);
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);

            oldTxns.setStatus("Settle");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);

            jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
            jsonObject.put("bonus", 0);
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            jsonObject.put("balance", BigDecimal.ZERO);
            jsonObject.put("bonus", 0);
            jsonObject.put("status", "INVALID_TOKEN_ID");
            return jsonObject;
        }
    }

    @Override
    public Object cancel(JSONObject params,String authToken , String ip) {
        logger.info("redtiger_cancel redtigerGame paramJson:{}, ip:{}", params, ip);
        JSONObject jsonObject = new JSONObject();
        String uuid = params.getString("uuid");
        jsonObject.put("status", "OK");
        jsonObject.put("uuid", uuid);

        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_TOKEN_ID");
                return jsonObject;
            }
            String playerID = params.getString("userId");
            if (null == playerID || "".equals(playerID)) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "TEMPORARY_ERROR");
                return jsonObject;
            }

            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
            if (null == memBaseinfo) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_PARAMETER");
                return jsonObject;
            }
            JSONObject transaction = params.getJSONObject("transaction");
            String platformTxId = transaction.getString("id");
            String refId = transaction.getString("refId");
//            JSONObject game = params.getJSONObject("game");
//            JSONObject table = game.getJSONObject("details").getJSONObject("table");


            // 查询用户请求订单
//            Txns oldTxns1 = getTxns(platformTxId, memBaseinfo.getAccount());
            BigDecimal balance = memBaseinfo.getBalance();
//            if(null!=oldTxns1){
//                jsonObject.put("balance", balance);
//                jsonObject.put("bonus", 0);
//                jsonObject.put("status", "BET_ALREADY_EXIST");
//                return jsonObject;
//            }
            // 查询用户请求订单
            Txns oldTxns = getTxnsByRoundId(refId, memBaseinfo.getAccount());
            if (null == oldTxns) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "BET_DOES_NOT_EXIST");
                return jsonObject;
            }

            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getMethod())||"Settle".equals(oldTxns.getMethod())) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "BET_ALREADY_SETTLED");
                return jsonObject;
            }
            BigDecimal betAmount = null!=transaction.getBigDecimal("amount")?transaction.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal amount = oldTxns.getBetAmount();

            // 取消金额大于下注金额
            if (betAmount.compareTo(amount) == 1) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INSUFFICIENT_FUNDS");
                return jsonObject;
            }else {
                balance = balance.add(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.REFUND, TradingEnum.INCOME);
            }

            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            //游戏商注单号
            txns.setPlatformTxId(platformTxId);
            txns.setBalance(balance);
            txns.setId(null);
            txns.setStatus("Running");
            txns.setWinAmount(amount);
            txns.setRealWinAmount(amount);//真实返还金额
            txns.setMethod("Cancel Bet");
            txns.setCreateTime(dateStr);
            if (betAmount.compareTo(amount) == 1) {
                Txns txns2 = new Txns();
                BeanUtils.copyProperties(oldTxns, txns2);
                //游戏商注单号
                txns2.setPlatformTxId(platformTxId);
                txns2.setBalance(balance);
                txns2.setId(null);
                txns2.setStatus("Running");
                txns2.setWinningAmount(amount.subtract(betAmount).negate());
                txns2.setWinAmount(amount);
                txns2.setRealWinAmount(amount);//真实返还金额
                txns2.setMethod("Place Bet");
                txns2.setCreateTime(dateStr);
                txnsMapper.insert(txns2);
                txns.setStatus("Place Bet");
            }
            txnsMapper.insert(txns);

            oldTxns.setStatus("Cancel Bet");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);
            // 中奖金额小于0


            jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
            jsonObject.put("bonus", 0);
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            jsonObject.put("balance", BigDecimal.ZERO);
            jsonObject.put("bonus", 0);
            jsonObject.put("status", "INVALID_TOKEN_ID");
            return jsonObject;
        }
    }

    @Override
    public Object promo_payout(JSONObject params,String authToken , String ip) {
        logger.info("redtiger_promo_payout  redtigerGame paramJson:{}, ip:{}", params, ip);
        JSONObject jsonObject = new JSONObject();
        String uuid = params.getString("uuid");
        jsonObject.put("status", "OK");
        jsonObject.put("uuid", uuid);
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_TOKEN_ID");
                return jsonObject;
            }
            String playerID = params.getString("userId");
            if (null == playerID || "".equals(playerID)) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "TEMPORARY_ERROR");
                return jsonObject;
            }

            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(playerID);
            if (null == memBaseinfo) {
//                jsonObject.put("balance", BigDecimal.ZERO);
//                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_PARAMETER");
                return jsonObject;
            }
            JSONObject promoTransaction = params.getJSONObject("promoTransaction");
            String platformTxId = promoTransaction.getString("id");
            JSONObject game = params.getJSONObject("game");
            JSONObject table = null;
            if(null!=game){
                table = game.getJSONObject("details").getJSONObject("table");
            }

            // 赢奖金额
            BigDecimal betAmount = BigDecimal.ZERO;
            String roundId = null, promotionId = null, promoType = promoTransaction.getString("type");
            GamePlatform gamePlatform;
            if("Y".equals(OpenAPIProperties.REDTIGER_IS_PLATFORM_LOGIN)){
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformGameParent.getPlatformCode(),platformGameParent.getPlatformCode());
            }else if(null!=table){
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(table.getString("id"), platformGameParent.getPlatformCode());

            }else {
                gamePlatform = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.REDTIGER_PLATFORM_CODE).get(0);
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            BigDecimal balance = memBaseinfo.getBalance();
            // 免费回合游戏
            if ("FreeRoundPlayableSpent".equals(promoType)) {
                betAmount = null!=promoTransaction.getBigDecimal("amount")?promoTransaction.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
                roundId = promoTransaction.getString("voucherId");
            } else if ("JackpotWin".equals(promoType)) {
                // 免费回合头奖
                roundId = game.getJSONObject("details").getJSONObject("table").getString("id");
                promotionId = roundId;
                JSONArray jsonArray = promoTransaction.getJSONArray("jackpots");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    betAmount.add(null!=jsonObject1.getBigDecimal("winAmount")?jsonObject1.getBigDecimal("winAmount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO);
                }
            } else if ("RewardGamePlayableSpent".equals(promoType)) {
                // 由于花费了所有奖励游戏代金券可玩余额而发放了促销支出
                betAmount = null!=promoTransaction.getBigDecimal("amount")?promoTransaction.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
                roundId = promoTransaction.getString("voucherId");
            } else if ("RewardGameWinCapReached".equals(promoType)) {
                // 由于达到奖励游戏的最大可能奖金而发放了促销支出
                betAmount = null!=promoTransaction.getBigDecimal("amount")?promoTransaction.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;;
                roundId = promoTransaction.getString("voucherId");
            } else if ("RewardGameMinBetLimitReached".equals(promoType)) {

                // 由于奖励游戏可玩余额达到当前牌桌的最小下注限制，因此发放了促销奖金。
                betAmount = null!=promoTransaction.getBigDecimal("amount")?promoTransaction.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;;
                roundId = promoTransaction.getString("voucherId");
            } else if ("RtrMonetaryReward".equals(promoType)) {
                // 由于游戏回合中的某些配置事件而发放了促销支出。
                betAmount = null!=promoTransaction.getBigDecimal("amount")?promoTransaction.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;;
                roundId = promoTransaction.getString("bonusConfigId");
            } else if ("roulette".equals(promoType)) {

                // 因在游戏回合中获胜而发放促销奖金。
                betAmount = null!=promoTransaction.getBigDecimal("amount")?promoTransaction.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;;
                roundId = game.getJSONObject("details").getJSONObject("table").getString("id");
                promotionId = roundId;
            }else {
                betAmount = null!=promoTransaction.getBigDecimal("amount")?promoTransaction.getBigDecimal("amount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;;
                roundId = game.getJSONObject("details").getJSONObject("table").getString("id");
            }

            // 赢奖金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INSUFFICIENT_FUNDS");
                return jsonObject;
            }

            // 查询用户请求订单
            Txns txns = getTxns(platformTxId, memBaseinfo.getAccount());
            if (null != txns) {
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
//                jsonObject.put("status", "BET_DOES_NOT_EXIST");
                return jsonObject;
            }
            txns = new Txns();
            // 会员余额
            balance = balance.add(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);

            //游戏商注单号
            txns.setPlatformTxId(platformTxId);
            //此交易是否是投注 true是投注 false 否
            txns.setBet(false);
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(params.getString("currency"));
            //游戏平台的下注项目
//            txns.setBetType(promoTransaction.getString("type"));
            // 奖金游戏
//            txns.setHasBonusGame(1);
            //平台代码
            txns.setPlatform(platformGameParent.getPlatformCode());
            //平台名称
            txns.setPlatformEnName(platformGameParent.getPlatformEnName());
            txns.setPlatformCnName(platformGameParent.getPlatformCnName());
            txns.setRoundId(roundId);
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
            txns.setBetAmount(BigDecimal.ZERO);
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(System.currentTimeMillis(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(BigDecimal.ZERO);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(betAmount);
            //返还金额 (包含下注金额)
            txns.setWinAmount(betAmount);
            // 活动派彩
            txns.setAmount(betAmount);
            // 活动ID
            txns.setPromotionId(promotionId);
            // 活动类型ID
            txns.setPromotionTypeId(promoType);
            //有效投注金额 或 投注面值
            txns.setTurnover(BigDecimal.ZERO);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(System.currentTimeMillis(), DateUtils.newFormat));
            //操作名称
            txns.setMethod("Bonus");
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
                jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
                jsonObject.put("bonus", 0);
                jsonObject.put("status", "INVALID_TOKEN_ID");
                return jsonObject;
            }

            jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
            jsonObject.put("bonus", 0);
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            jsonObject.put("balance", BigDecimal.ZERO);
            jsonObject.put("bonus", 0);
            jsonObject.put("status", "INVALID_TOKEN_ID");
            return jsonObject;
        }
    }

    /**
     * 获取历史订单
     *
     * @param reference 订单编号
     * @param userId    会员ID
     * @return 订单
     */
    private Txns getTxns(String reference, String userId) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Bonus")
                .or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, reference);
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.REDTIGER_PLATFORM_CODE);
//        wrapper.eq(Txns::getUserId, userId);
        return txnsMapper.selectOne(wrapper);
    }
    private Txns getTxnsByRoundId(String roundId, String userId) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Bonus")
                .or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getRoundId, roundId);
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.REDTIGER_PLATFORM_CODE);
//        wrapper.eq(Txns::getUserId, userId);
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

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.REDTIGER_PLATFORM_CODE);
    }
}
