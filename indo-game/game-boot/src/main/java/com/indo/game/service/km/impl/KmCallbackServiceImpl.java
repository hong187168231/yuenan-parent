package com.indo.game.service.km.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.km.KmCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * kingmaker
 *
 * @author
 */
@Service
public class KmCallbackServiceImpl implements KmCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;


    @Override
    public Object kmBalanceCallback(JSONObject jsonObject, String ip) {
        JSONArray array =  jsonObject.getJSONArray("users");
        JSONArray jsonArray = new JSONArray();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.KM_PLATFORM_CODE);
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(json.getString("userid"));
                JSONObject dataJson = new JSONObject();
                if (null == memBaseinfo) {
                    dataJson.put("err", "50");
                    dataJson.put("errdesc", "Currency Mismatch");
                    jsonArray.add(dataJson);
                } else {
                    JSONArray arrayList = new JSONArray();
                    JSONObject object = new JSONObject();
                    object.put("code", json.getString("Mainwallet"));
                    object.put("bal", memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
                    object.put("cur", json.getString("cur"));
                    object.put("name", "Mainwallet");
                    object.put("desc", memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
                    arrayList.add(object);
                    dataJson.put("userid", json.getString("userid"));
                    dataJson.put("wallets", arrayList);
                    jsonArray.add(dataJson);
                }
            }
        }
        JSONObject  json = new JSONObject();
        json.put("users", jsonArray);
        return json;
    }
    //扣钱
    @Override
    public Object kmDebitCallback(JSONObject jsonObject, String ip) {
        JSONArray array = jsonObject.getJSONArray("transactions");
        JSONArray jsonArray = new JSONArray();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.KM_PLATFORM_CODE);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.KM_PLATFORM_CODE,gameParentPlatform.getPlatformCode());;
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                logger.info("kmDebitCallback json=="+i+":{}", JSONObject.toJSONString(json));
                BigDecimal amt = null!=json.getBigDecimal("amt")?json.getBigDecimal("amt").multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(json.getString("userid"));
                BigDecimal balance = memBaseinfo.getBalance();
                JSONObject dataJson = new JSONObject();
                if (null == memBaseinfo) {
                    dataJson.put("err", " 10");
                    dataJson.put("errdesc", "Token has expired");
                    jsonArray.add(dataJson);
                }
//                500 投注(Place bet)
//                510 赢钱(Win bet)
//                511 贏彩金(Win Jackpot)
//                520 输钱(Lose bet) 未使用。 0值表示交易未送出。
//                530 免费投注(Free bet)
//                540 平手(Tie bet)
//                560 取消交易
//                590 结束局(End Round)
//                600 电子钱包加钱 (Fund in the player’s wallet)
//                610 电子钱包扣钱 (Fund out the player’s wallet)
//                611 取消电子钱包扣钱 (Cancel fund-in)
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
                wrapper.eq(Txns::getStatus, "Running");

                wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
                Integer  txtype = json.getInteger("txtype");
                String ptxid = json.getString("ptxid");
                String refptxid = json.getString("refptxid");
                String cur = json.getString("cur");
                String roundid = json.getString("roundid");

                String method = "Place Bet";
                boolean b = true;//是否新增 true新增
                Txns oldTxns = new Txns();
                if(500==txtype || 530==txtype){//500 投注(Place bet) 530 免费投注(Free bet)
                    wrapper.eq(Txns::getPlatformTxId, ptxid);
                    oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns && 500==txtype){
                        if (memBaseinfo.getBalance().compareTo(amt) == -1) {
                            dataJson.put("err", 100);
                            dataJson.put("errdesc", "资金不足，无法执行操作。");
                            jsonArray.add(dataJson);
                            continue;
                        }
                        if (amt.compareTo(BigDecimal.ZERO) != 0) {
                            balance = balance.subtract(amt);
                            gameCommonService.updateUserBalance(memBaseinfo, amt, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                        }
                    }else if("Place Bet".equals(oldTxns.getMethod())) {
                        dataJson.put("txid", ptxid);
                        dataJson.put("bal", balance.divide(gameParentPlatform.getCurrencyPro()));
                        dataJson.put("ptxid", ptxid);
                        dataJson.put("cur", cur);
                        dataJson.put("dup", "true");
                        jsonArray.add(dataJson);
                        continue;
                    }else if("Cancel Bet".equals(oldTxns.getMethod())) {
                        dataJson.put("err", 610);
                        dataJson.put("errdesc", "交易已被取消");
                        jsonArray.add(dataJson);
                        continue;
                    }else if("Settle".equals(oldTxns.getMethod())) {
                        dataJson.put("err", 800);
                        dataJson.put("errdesc", "确定性的操作失败。");
                        jsonArray.add(dataJson);
                        continue;
                    }

                }
                //                510 赢钱(Win bet)
                //                511 贏彩金(Win Jackpot)
                //                520 输钱(Lose bet) 未使用。 0值表示交易未送出。
                //                540 平手(Tie bet)
                //                590 结束局(End Round)
                //                600 电子钱包加钱 (Fund in the player’s wallet)
                //                610 电子钱包扣钱 (Fund out the player’s wallet)
                if(510==txtype || 511==txtype || 520==txtype || 540==txtype || 590==txtype || 600==txtype || 610==txtype){
                    wrapper.eq(Txns::getPlatformTxId, refptxid);
                    oldTxns = txnsMapper.selectOne(wrapper);
                    method = "Settle";
                    if(null==oldTxns){
                        if (memBaseinfo.getBalance().compareTo(amt) == -1) {
                            dataJson.put("err", 100);
                            dataJson.put("errdesc", "资金不足，无法执行操作。");
                            jsonArray.add(dataJson);
                            continue;
                        }
                        if (amt.compareTo(BigDecimal.ZERO) != 0) {
                            balance = balance.subtract(amt);
                            gameCommonService.updateUserBalance(memBaseinfo, amt, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                        }
                    }else if("Settle".equals(oldTxns.getMethod())) {
                        dataJson.put("txid", ptxid);
                        dataJson.put("bal", balance.divide(gameParentPlatform.getCurrencyPro()));
                        dataJson.put("ptxid", ptxid);
                        dataJson.put("cur", cur);
                        dataJson.put("dup", "true");
                        jsonArray.add(dataJson);
                        continue;
                    }else if("Cancel Bet".equals(oldTxns.getMethod())) {
                        dataJson.put("err", 610);
                        dataJson.put("errdesc", "交易已被取消");
                        jsonArray.add(dataJson);
                        continue;
                    }else {
                        if (memBaseinfo.getBalance().compareTo(amt) == -1) {
                            dataJson.put("err", 100);
                            dataJson.put("errdesc", "资金不足，无法执行操作。");
                            jsonArray.add(dataJson);
                            continue;
                        }
                        if (amt.compareTo(BigDecimal.ZERO) != 0) {
                            balance = balance.subtract(amt);
                            gameCommonService.updateUserBalance(memBaseinfo, amt, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                        }
                        b = false;
                    }
                }
                //                560 取消交易
                //                611 取消电子钱包扣钱 (Cancel fund-in)
                if(560==txtype || 611==txtype){
                    method = "Cancel Bet";
                    wrapper.eq(Txns::getPlatformTxId, refptxid);
                    oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
                        dataJson.put("txid", ptxid);
                        dataJson.put("bal", balance.divide(gameParentPlatform.getCurrencyPro()));
                        dataJson.put("ptxid", ptxid);
                        dataJson.put("cur", cur);
                        dataJson.put("dup", "false");
                        jsonArray.add(dataJson);
                        continue;
                    }else if("Cancel Bet".equals(oldTxns.getMethod())) {
                        dataJson.put("txid", ptxid);
                        dataJson.put("bal", balance.divide(gameParentPlatform.getCurrencyPro()));
                        dataJson.put("ptxid", ptxid);
                        dataJson.put("cur", cur);
                        dataJson.put("dup", "true");
                        jsonArray.add(dataJson);
                        continue;
                    }else  {
                        if (memBaseinfo.getBalance().compareTo(amt) == -1) {
                            dataJson.put("err", 100);
                            dataJson.put("errdesc", "资金不足，无法执行操作。");
                            jsonArray.add(dataJson);
                            continue;
                        }
                        if (amt.compareTo(BigDecimal.ZERO) != 0) {
                            balance = balance.subtract(amt);
                            if("Place Bet".equals(oldTxns.getMethod())) {
                                gameCommonService.updateUserBalance(memBaseinfo, amt, GoldchangeEnum.CANCEL_BET, TradingEnum.SPENDING);
                            }else {
                                gameCommonService.updateUserBalance(memBaseinfo, amt, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
                            }
                        }
                        b = false;
                    }
                }
                //创建时间
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                Txns txns = new Txns();
                if(b){
                    //玩家货币代码
                    txns.setCurrency(gameParentPlatform.getCurrencyType());
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
                    //游戏名称
                    txns.setGameName(gamePlatform.getPlatformEnName());
                    //下注金额
                    txns.setBetAmount(amt.negate());

                }else {
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setId(null);
                    //操作名称
                    oldTxns.setStatus(method);
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                }
                //游戏商注单号
                txns.setPlatformTxId(ptxid);
                txns.setRePlatformTxId(refptxid);
                //玩家 ID
                txns.setUserId(memBaseinfo.getAccount());
                txns.setRoundId(roundid);

                txns.setMpId(txtype);
                //操作名称
                txns.setMethod(method);
                txns.setStatus("Running");
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(amt.negate());
                txns.setWinAmount(amt);
                //余额
                txns.setBalance(balance);
                txns.setCreateTime(dateStr);
                //投注 IP
                txns.setBetIp(ip);//  string 是 投注 IP
                int num = txnsMapper.insert(txns);

                dataJson.put("txid", ptxid);
                dataJson.put("bal", balance.divide(gameParentPlatform.getCurrencyPro()));
                dataJson.put("ptxid", ptxid);
                dataJson.put("cur", cur);
                dataJson.put("dup", "false");
                jsonArray.add(dataJson);
            }
        }
        JSONObject object = new JSONObject();
        object.put("transactions", jsonArray);
        return object;
    }
//加款
    @Override
    public Object kmCreditCallback(JSONObject jsonObject, String ip) {
        JSONArray array = jsonObject.getJSONArray("transactions");
        JSONArray jsonArray = new JSONArray();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.KM_PLATFORM_CODE);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.KM_PLATFORM_CODE,gameParentPlatform.getPlatformCode());;
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                logger.info("kmCreditCallback json=="+i+":{}", JSONObject.toJSONString(json));
                BigDecimal amt = null!=json.getBigDecimal("amt")?json.getBigDecimal("amt").multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                if(null==amt){
                    amt = BigDecimal.ZERO;
                }
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(json.getString("userid"));
                BigDecimal balance = memBaseinfo.getBalance();
                JSONObject dataJson = new JSONObject();
                if (null == memBaseinfo) {
                    dataJson.put("err", " 10");
                    dataJson.put("errdesc", "Token has expired");
                    jsonArray.add(dataJson);
                }
                //                500 投注(Place bet)
//                510 赢钱(Win bet)
//                511 贏彩金(Win Jackpot)
//                520 输钱(Lose bet) 未使用。 0值表示交易未送出。
//                530 免费投注(Free bet)
//                540 平手(Tie bet)
//                560 取消交易
//                590 结束局(End Round)
//                600 电子钱包加钱 (Fund in the player’s wallet)
//                610 电子钱包扣钱 (Fund out the player’s wallet)
//                611 取消电子钱包扣钱 (Cancel fund-in)
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
                wrapper.eq(Txns::getStatus, "Running");

                wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
                Integer  txtype = json.getInteger("txtype");
                String ptxid = json.getString("ptxid");
                String refptxid = json.getString("refptxid");
                String cur = json.getString("cur");
                String roundid = json.getString("roundid");
                String method = "Place Bet";
                Txns oldTxns = new Txns();
                boolean b = true;//是否新增 true新增
                if(500==txtype || 530==txtype){//500 投注(Place bet) 530 免费投注(Free bet)
                    wrapper.eq(Txns::getPlatformTxId, ptxid);
                    oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns && 500==txtype){
                        if (amt.compareTo(BigDecimal.ZERO) != 0) {
                            balance = balance.add(amt);
                            gameCommonService.updateUserBalance(memBaseinfo, amt, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
                        }
                    }else if("Place Bet".equals(oldTxns.getMethod())) {
                        dataJson.put("txid", ptxid);
                        dataJson.put("bal", balance.divide(gameParentPlatform.getCurrencyPro()));
                        dataJson.put("ptxid", ptxid);
                        dataJson.put("cur", cur);
                        dataJson.put("dup", "false");
                        jsonArray.add(dataJson);
                        continue;
                    }else if("Cancel Bet".equals(oldTxns.getMethod())) {
                        dataJson.put("err", 610);
                        dataJson.put("errdesc", "交易已被取消");
                        jsonArray.add(dataJson);
                        continue;
                    }else if("Settle".equals(oldTxns.getMethod())) {
                        dataJson.put("err", 800);
                        dataJson.put("errdesc", "确定性的操作失败。");
                        jsonArray.add(dataJson);
                        continue;
                    }

                }
                //                510 赢钱(Win bet)
                //                511 贏彩金(Win Jackpot)
                //                520 输钱(Lose bet) 未使用。 0值表示交易未送出。
                //                540 平手(Tie bet)
                //                590 结束局(End Round)
                //                600 电子钱包加钱 (Fund in the player’s wallet)
                //                610 电子钱包扣钱 (Fund out the player’s wallet)
                if(510==txtype || 511==txtype || 520==txtype || 540==txtype || 590==txtype || 600==txtype || 610==txtype){
                    wrapper.eq(Txns::getPlatformTxId, refptxid);
                    oldTxns = txnsMapper.selectOne(wrapper);
                    method = "Settle";
                    if(null==oldTxns){
                        if (amt.compareTo(BigDecimal.ZERO) != 0) {
                            balance = balance.add(amt);
                            gameCommonService.updateUserBalance(memBaseinfo, amt, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                        }
                    }else if("Settle".equals(oldTxns.getMethod())) {
                        dataJson.put("txid", ptxid);
                        dataJson.put("bal", balance.divide(gameParentPlatform.getCurrencyPro()));
                        dataJson.put("ptxid", ptxid);
                        dataJson.put("cur", cur);
                        dataJson.put("dup", "true");
                        jsonArray.add(dataJson);
                        continue;
                    }else if("Cancel Bet".equals(oldTxns.getMethod())) {
                        dataJson.put("err", 610);
                        dataJson.put("errdesc", "交易已被取消");
                        jsonArray.add(dataJson);
                        continue;
                    }else {
                        if (amt.compareTo(BigDecimal.ZERO) != 0) {
                            balance = balance.add(amt);
                            gameCommonService.updateUserBalance(memBaseinfo, amt, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                        }
                        b = false;
                    }
                }
                //                560 取消交易
                //                611 取消电子钱包扣钱 (Cancel fund-in)
                if(560==txtype || 611==txtype){
                    wrapper.eq(Txns::getPlatformTxId, refptxid);
                    oldTxns = txnsMapper.selectOne(wrapper);
                    method = "Cancel Bet";
                    if(null==oldTxns){
                        dataJson.put("txid", ptxid);
                        dataJson.put("bal", balance.divide(gameParentPlatform.getCurrencyPro()));
                        dataJson.put("ptxid", ptxid);
                        dataJson.put("cur", cur);
                        dataJson.put("dup", "true");
                        jsonArray.add(dataJson);
                        continue;
                    }else if("Cancel Bet".equals(oldTxns.getMethod())) {
                        dataJson.put("txid", ptxid);
                        dataJson.put("bal", balance.divide(gameParentPlatform.getCurrencyPro()));
                        dataJson.put("ptxid", ptxid);
                        dataJson.put("cur", cur);
                        dataJson.put("dup", "true");
                        jsonArray.add(dataJson);
                        continue;
                    }else {
                        if (amt.compareTo(BigDecimal.ZERO) != 0) {
                            if (amt.compareTo(BigDecimal.ZERO) != 0) {
                                balance = balance.add(amt);
                                if("Place Bet".equals(oldTxns.getMethod())) {
                                    gameCommonService.updateUserBalance(memBaseinfo, amt, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                                }else {
                                    gameCommonService.updateUserBalance(memBaseinfo, amt, GoldchangeEnum.UNSETTLE, TradingEnum.INCOME);
                                }
                            }
                        }
                        b = false;
                    }
                }
                //创建时间
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                Txns txns = new Txns();
                if(b){
                    //玩家货币代码
                    txns.setCurrency(gameParentPlatform.getCurrencyType());
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
                    //游戏名称
                    txns.setGameName(gamePlatform.getPlatformEnName());
                    //下注金额
                    txns.setBetAmount(amt.negate());

                }else {
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setId(null);
                    //操作名称
                    oldTxns.setStatus(method);
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                }
                //游戏商注单号
                txns.setPlatformTxId(ptxid);
                txns.setRePlatformTxId(refptxid);
                //玩家 ID
                txns.setUserId(memBaseinfo.getAccount());
                txns.setRoundId(roundid);

                txns.setMpId(txtype);
                //操作名称
                txns.setMethod(method);
                txns.setStatus("Running");
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(amt);
                txns.setWinAmount(amt);
                //余额
                txns.setBalance(balance);
                txns.setCreateTime(dateStr);
                //投注 IP
                txns.setBetIp(ip);//  string 是 投注 IP
                int num = txnsMapper.insert(txns);

                dataJson.put("txid", ptxid);
                dataJson.put("bal", balance.divide(gameParentPlatform.getCurrencyPro()));
                dataJson.put("ptxid", ptxid);
                dataJson.put("cur", cur);
                dataJson.put("dup", "false");
                jsonArray.add(dataJson);
            }
        }
        JSONObject object = new JSONObject();
        object.put("transactions", jsonArray);
        return object;
    }
}


