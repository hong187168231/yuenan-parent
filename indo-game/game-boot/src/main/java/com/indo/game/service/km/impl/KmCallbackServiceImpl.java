package com.indo.game.service.km.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.km.KmCallbackService;
import com.indo.user.pojo.bo.MemTradingBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;


/**
 * DG
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
        JSONArray array = JSONObject.parseArray(jsonObject.getString("users"));
        JSONArray jsonArray = new JSONArray();
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
                    object.put("code", json.getString("walletcode"));
                    object.put("bal", memBaseinfo.getBalance());
                    object.put("cur", json.getString("cur"));
                    object.put("name", memBaseinfo.getAccount());
                    object.put("desc", memBaseinfo.getBalance());
                    arrayList.add(object);
                    dataJson.put("userid", json.getString("userid"));
                    dataJson.put("wallets", arrayList);
                    jsonArray.add(dataJson);
                }
            }
        }
        return jsonArray;
    }

    @Override
    public Object kmDebitCallback(JSONObject jsonObject, String ip) {
        JSONArray array = JSONObject.parseArray(jsonObject.getString("transactions"));
        JSONArray jsonArray = new JSONArray();
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(json.getString("userid"));
                BigDecimal balance = memBaseinfo.getBalance();
                JSONObject dataJson = new JSONObject();
                if (null == memBaseinfo) {
                    dataJson.put("err", " 10");
                    dataJson.put("errdesc", "Token has expired");
                    jsonArray.add(dataJson);
                }
                if ("Gao_Gae".equals(json.getString("gamecode")) || "Kingmaker_Pok_Deng".equals(json.getString("gamecode"))
                        || "Pai_Kang".equals(json.getString("gamecode")) || "Blackjack".equals(json.getString("gamecode"))
                        || "Teen_Patti".equals(json.getString("gamecode")) || "Five_Card_Poker".equals(json.getString("gamecode"))) {
                    if ("500".equals(json.getString("txtype")) || "530".equals(json.getString("txtype")) || "540".equals(json.getString("txtype"))) {
                        dataJson.put("txid", json.getString("ptxid"));
                        dataJson.put("ptxid", json.getString("ptxid"));
                        dataJson.put("bal", memBaseinfo.getBalance());
                        dataJson.put("cur", json.getString("cur"));
                        dataJson.put("dup", "false");
                        jsonArray.add(dataJson);
                        continue;
                    } else {
                        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
                        wrapper.eq(Txns::getStatus, "Running");
                        wrapper.eq(Txns::getPlatformTxId, json.getString("ptxid"));
                        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
                        Txns oldTxns = txnsMapper.selectOne(wrapper);
                        if (null != oldTxns) {
                            dataJson.put("txid", json.getString("ptxid"));
                            dataJson.put("ptxid", json.getString("ptxid"));
                            dataJson.put("dup", "true");
                            jsonArray.add(dataJson);
                            continue;
                        }
                        if (memBaseinfo.getBalance().compareTo(json.getBigDecimal("amt")) == -1) {
                            dataJson.put("err", 10);
                            dataJson.put("errdesc", "Token has expired");
                            jsonArray.add(dataJson);
                            continue;
                        }
                        balance = balance.subtract(json.getBigDecimal("amt"));
                        gameCommonService.updateUserBalance(memBaseinfo, jsonObject.getBigDecimal("amt"), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                    }

                } else {
                    if ("530".equals(json.getString("txtype")) || "540".equals(json.getString("txtype"))) {
                        dataJson.put("txid", json.getString("ptxid"));
                        dataJson.put("bal", memBaseinfo.getBalance());
                        dataJson.put("ptxid", json.getString("ptxid"));
                        dataJson.put("cur", json.getString("cur"));
                        dataJson.put("dup", "false");
                        jsonArray.add(dataJson);
                        continue;
                    }
                    if (memBaseinfo.getBalance().compareTo(json.getBigDecimal("amt")) == -1) {
                        dataJson.put("err", 10);
                        dataJson.put("errdesc", "Token has expired");
                        jsonArray.add(dataJson);
                        continue;
                    }
                    balance = balance.subtract(json.getBigDecimal("amt"));
                    gameCommonService.updateUserBalance(memBaseinfo, jsonObject.getBigDecimal("amt"), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                }

                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(json.getString("ptxid"));
                //玩家 ID
                txns.setUserId(memBaseinfo.getId().toString());

                txns.setRoundId(json.getString("roundid"));

                txns.setMpId(json.getInteger("txtype"));
                //平台代码
                txns.setPlatform("DG");
                //下注金额
                txns.setBetAmount(jsonObject.getBigDecimal("amt"));
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(jsonObject.getBigDecimal("amt"));
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(jsonObject.getBigDecimal("amt"));
                //真实返还金额,游戏赢分
                txns.setRealWinAmount(jsonObject.getBigDecimal("amt"));
                //返还金额 (包含下注金额)
                //赌注的结果 : 赢:0,输:1,平手:2
                int resultTyep;
                //有效投注金额 或 投注面值
                txns.setTurnover(jsonObject.getBigDecimal("amt"));
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
                    dataJson.put("txid", json.getString("ptxid"));
                    dataJson.put("bal", memBaseinfo.getBalance());
                    dataJson.put("ptxid", json.getString("ptxid"));
                    dataJson.put("cur", json.getString("cur"));
                    dataJson.put("dup", "false");
                    jsonArray.add(dataJson);
                    continue;
                }

                dataJson.put("txid", json.getString("ptxid"));
                dataJson.put("bal", memBaseinfo.getBalance());
                dataJson.put("ptxid", json.getString("ptxid"));
                dataJson.put("cur", json.getString("cur"));
                dataJson.put("dup", "false");
                jsonArray.add(dataJson);
            }
        }
        JSONObject object = new JSONObject();
        object.put("transactions", jsonArray);
        return object;
    }

    @Override
    public Object kmCreditCallback(JSONObject jsonObject, String ip) {
        JSONArray array = JSONObject.parseArray(jsonObject.getString("transactions"));
        JSONArray jsonArray = new JSONArray();
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(json.getString("userid"));
                BigDecimal balance = memBaseinfo.getBalance();
                JSONObject dataJson = new JSONObject();
                if (null == memBaseinfo) {
                    dataJson.put("err", " 10");
                    dataJson.put("errdesc", "Token has expired");
                    jsonArray.add(dataJson);
                }
                if ("Gao_Gae".equals(json.getString("gamecode")) || "Kingmaker_Pok_Deng".equals(json.getString("gamecode"))
                        || "Pai_Kang".equals(json.getString("gamecode")) || "Blackjack".equals(json.getString("gamecode"))
                        || "Teen_Patti".equals(json.getString("gamecode")) || "Five_Card_Poker".equals(json.getString("gamecode"))) {
                    if ("510".equals(json.getString("txtype")) || "540".equals(json.getString("txtype"))) {
                        dataJson.put("txid", json.getString("ptxid"));
                        dataJson.put("ptxid", json.getString("ptxid"));
                        dataJson.put("bal", memBaseinfo.getBalance());
                        dataJson.put("cur", json.getString("cur"));
                        dataJson.put("dup", "false");
                        jsonArray.add(dataJson);
                        continue;
                    } else if ("520".equals(json.getString("txtype")) ) {
                        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
                        wrapper.eq(Txns::getStatus, "Running");
                        wrapper.eq(Txns::getRoundId, json.getString("roundid"));
                        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
                        List<Txns> oldTxnsList = txnsMapper.selectList(wrapper);
                        if (oldTxnsList.size() <= 0) {
                            dataJson.put("err", " 10");
                            dataJson.put("errdesc", "Token has expired");
                            jsonArray.add(dataJson);
                        }
                        BigDecimal addBigDecimal = BigDecimal.ZERO;
                        BigDecimal betBigDecimal = BigDecimal.ZERO;
                        for (Txns txns : oldTxnsList) {
                            if (txns.getMpId() == 610) {
                                addBigDecimal = txns.getBetAmount();
                            }
                            if (txns.getMpId() == 500) {
                                betBigDecimal = txns.getBetAmount();
                            }
                        }
                        if (json.getBigDecimal("amt").compareTo(betBigDecimal) == -1) {
                            BigDecimal money = addBigDecimal.subtract(betBigDecimal);
                            balance = balance.add(money);
                            gameCommonService.updateUserBalance(memBaseinfo, money, GoldchangeEnum.REFUND, TradingEnum.INCOME);
                        }
                    } else {
                        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
                        wrapper.eq(Txns::getStatus, "Running");
                        wrapper.eq(Txns::getPlatformTxId, json.getString("ptxid"));
                        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
                        Txns oldTxns = txnsMapper.selectOne(wrapper);
                        if (null != oldTxns) {
                            dataJson.put("txid", json.getString("ptxid"));
                            dataJson.put("ptxid", json.getString("ptxid"));
                            dataJson.put("dup", "true");
                            jsonArray.add(dataJson);
                            continue;
                        }
                        balance = balance.add(jsonObject.getBigDecimal("amt"));
                        gameCommonService.updateUserBalance(memBaseinfo, jsonObject.getBigDecimal("amt"), GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
                    }

                } else {
                    if ("530".equals(json.getString("txtype")) || "540".equals(json.getString("txtype"))) {
                        dataJson.put("txid", json.getString("ptxid"));
                        dataJson.put("bal", memBaseinfo.getBalance());
                        dataJson.put("ptxid", json.getString("ptxid"));
                        dataJson.put("cur", json.getString("cur"));
                        dataJson.put("dup", "false");
                        jsonArray.add(dataJson);
                        continue;
                    }
                    balance = balance.add(jsonObject.getBigDecimal("amt"));
                    gameCommonService.updateUserBalance(memBaseinfo, jsonObject.getBigDecimal("amt"), GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
                }

                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(json.getString("ptxid"));
                //玩家 ID
                txns.setUserId(memBaseinfo.getId().toString());

                txns.setRoundId(json.getString("roundid"));
                //平台代码
                txns.setPlatform("KingMaker");
                //下注金额
                txns.setBetAmount(jsonObject.getBigDecimal("amt"));
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(jsonObject.getBigDecimal("amt"));
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(jsonObject.getBigDecimal("amt"));
                //真实返还金额,游戏赢分
                txns.setRealWinAmount(jsonObject.getBigDecimal("amt"));
                //返还金额 (包含下注金额)
                //赌注的结果 : 赢:0,输:1,平手:2
                int resultTyep;
                //有效投注金额 或 投注面值
                txns.setTurnover(jsonObject.getBigDecimal("amt"));
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
                    dataJson.put("txid", json.getString("ptxid"));
                    dataJson.put("bal", memBaseinfo.getBalance());
                    dataJson.put("ptxid", json.getString("ptxid"));
                    dataJson.put("cur", json.getString("cur"));
                    dataJson.put("dup", "false");
                    jsonArray.add(dataJson);
                    continue;
                }

                dataJson.put("txid", json.getString("ptxid"));
                dataJson.put("bal", memBaseinfo.getBalance());
                dataJson.put("ptxid", json.getString("ptxid"));
                dataJson.put("cur", json.getString("cur"));
                dataJson.put("dup", "false");
                jsonArray.add(dataJson);
            }
        }
        JSONObject object = new JSONObject();
        object.put("transactions", jsonArray);
        return object;
    }
}


