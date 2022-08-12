package com.indo.game.service.v8.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.encrypt.MD5Encoder;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.core.service.game.common.GameCommonService;
import com.indo.game.service.v8.V8CallbackService;
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
public class V8CallbackServiceImpl implements V8CallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GameCommonService gameCommonService;

    @Autowired
    private TxnsMapper txnsMapper;


    @Override
    public Object getBalance(String agent, String timestamp, String account, String key, String ip,int s) {
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initResponse(s,4,account,BigDecimal.ZERO);
            }

            // 渠道不存在（请检查渠道 ID 是否正确）
            if (!OpenAPIProperties.V8_AGENT.equals(agent)) {
                return initResponse(s,10,account,BigDecimal.ZERO);
            }

            // 渠道验证错误
//            if (!key.equals(getKey(Long.parseLong(account)))) {
//                return initFailureResponse(15, "key 验证失败");
//            }
//
//            // 验证时间超时 , 大于30秒
//            if (checkTimestamp(Long.parseLong(timestamp))) {
//                return initFailureResponse(3, "验证时间超时");
//            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
            if (null == memBaseinfo) {
                return initResponse(s,2,account,BigDecimal.ZERO);
            }

            // 会员余额返回
            if (memBaseinfo.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                return initResponse(s,0,account,memBaseinfo.getBalance());
            }else {
                return initResponse(s,1,account,memBaseinfo.getBalance());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initResponse(s,3,account,BigDecimal.ZERO);
        }
    }

    @Override
    public Object debit(String agent, String timestamp, String account, String key, BigDecimal money, String ip,int s,String orderId,String gameNo) {

        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initResponse(s, 4, account, BigDecimal.ZERO);
            }

            // 渠道不存在（请检查渠道 ID 是否正确）
            if (!OpenAPIProperties.V8_AGENT.equals(agent)) {
                return initResponse(s, 10, account, BigDecimal.ZERO);
            }

//            // 渠道验证错误
//            if (!key.equals(getKey(Long.parseLong(account)))) {
//                return initFailureResponse(15, "key 验证失败");
//            }
//
//            // 验证时间超时 , 大于30秒
//            if (checkTimestamp(Long.parseLong(timestamp))) {
//                return initFailureResponse(3, "验证时间超时");
//            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
            if (null == memBaseinfo) {
                return initResponse(s, 2, account, BigDecimal.ZERO);
            }

            BigDecimal balance = memBaseinfo.getBalance();
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getMethod, "Place Bet");
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getPlatformTxId, orderId);
            wrapper.eq(Txns::getPlatform, platformGameParent.getPlatformCode());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if(null!=oldTxns){
                return initResponse(s,0,account,memBaseinfo.getBalance());
            }
            if (memBaseinfo.getBalance().compareTo(balance) < 0) {
                return initResponse(s, 1, account, memBaseinfo.getBalance());
            } else{

                if(BigDecimal.ZERO.compareTo(money)!=0){
                    balance = balance.subtract(money);
                    // 更新余额
                    gameCommonService.updateUserBalance(memBaseinfo, money, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                }
                // 生成订单数据
                Txns txns = getInitTxns(platformGameParent, orderId, account, money, balance, ip);
                int num = txnsMapper.insert(txns);

                return initResponse(s,0,account,memBaseinfo.getBalance());

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        return initResponse(s,3,account,BigDecimal.ZERO);
        }
    }

    @Override
    public Object settle(String agent, String timestamp, String account, String key, BigDecimal money, String ip,int s,String orderId,String gameNo) {

        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initResponse(s, 4, account, BigDecimal.ZERO);
            }

            // 渠道不存在（请检查渠道 ID 是否正确）
            if (!OpenAPIProperties.V8_AGENT.equals(agent)) {
                return initResponse(s, 10, account, BigDecimal.ZERO);
            }

//            // 渠道验证错误
//            if (!key.equals(getKey(Long.parseLong(account)))) {
//                return initFailureResponse(15, "key 验证失败");
//            }
//
//            // 验证时间超时 , 大于30秒
//            if (checkTimestamp(Long.parseLong(timestamp))) {
//                return initFailureResponse(3, "验证时间超时");
//            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
            if (null == memBaseinfo) {
                return initResponse(s, 2, account, BigDecimal.ZERO);
            }

            BigDecimal balance = memBaseinfo.getBalance();
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Settle"));
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getPlatformTxId, orderId);
            wrapper.eq(Txns::getPlatform, platformGameParent.getPlatformCode());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if(null!=oldTxns&&"Settle".equals(oldTxns.getMethod())){
                return initResponse(s,0,account,memBaseinfo.getBalance());
            }
            if (memBaseinfo.getBalance().compareTo(balance) < 0) {
                return initResponse(s, 1, account, memBaseinfo.getBalance());
            } else{

                if(BigDecimal.ZERO.compareTo(money)!=0){
                    balance = balance.add(money);
                    // 更新余额
                    gameCommonService.updateUserBalance(memBaseinfo, money, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                }
                Txns txns = new Txns();
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                txns.setCreateTime(dateStr);
                if(null!=oldTxns){

                    BeanUtils.copyProperties(oldTxns, txns);
                    oldTxns.setStatus("Settle");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                }
                txns.setWinningAmount(money);
                txns.setWinAmount(money);
                //余额
                txns.setBalance(balance);
                //操作名称
                txns.setMethod("Place Bet");
                txns.setStatus("Running");
                // 生成订单数据
                int num = txnsMapper.insert(txns);

                return initResponse(s,0,account,memBaseinfo.getBalance());

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initResponse(s,3,account,BigDecimal.ZERO);
        }
    }

    @Override
    public Object queryStatus(String agent, String timestamp, String account, String key, String ip,int s,String orderId){
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initResponse(s, 4, 5);
            }

            // 渠道不存在（请检查渠道 ID 是否正确）
            if (!OpenAPIProperties.V8_AGENT.equals(agent)) {
                return initResponse(s, 10, 5);
            }

//            // 渠道验证错误
//            if (!key.equals(getKey(Long.parseLong(account)))) {
//                return initFailureResponse(15, "key 验证失败");
//            }
//
//            // 验证时间超时 , 大于30秒
//            if (checkTimestamp(Long.parseLong(timestamp))) {
//                return initFailureResponse(3, "验证时间超时");
//            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
            if (null == memBaseinfo) {
                return initResponse(s, 2, account, BigDecimal.ZERO);
            }

            BigDecimal balance = memBaseinfo.getBalance();
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getPlatformTxId, orderId);
            wrapper.eq(Txns::getPlatform, platformGameParent.getPlatformCode());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if(null!=oldTxns){
                return initResponse(s,0,1);
            }else {
                return initResponse(s,0,4);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initResponse(s,3,2);
        }
    }

    @Override
    public Object cancelBet(String agent, String timestamp, String account, String key, BigDecimal money, String ip,int s,String orderId) {

        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initResponse(s, 4, 5);
            }

            // 渠道不存在（请检查渠道 ID 是否正确）
            if (!OpenAPIProperties.V8_AGENT.equals(agent)) {
                return initResponse(s, 10, 5);
            }

//            // 渠道验证错误
//            if (!key.equals(getKey(Long.parseLong(account)))) {
//                return initFailureResponse(15, "key 验证失败");
//            }
//
//            // 验证时间超时 , 大于30秒
//            if (checkTimestamp(Long.parseLong(timestamp))) {
//                return initFailureResponse(3, "验证时间超时");
//            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
            if (null == memBaseinfo) {
                return initResponse(s, 2, 5);
            }

            BigDecimal balance = memBaseinfo.getBalance();
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Settle").or().eq(Txns::getMethod, "Cancel Bet"));
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getPlatformTxId, orderId);
            wrapper.eq(Txns::getPlatform, platformGameParent.getPlatformCode());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if(null==oldTxns){
                return initResponse(s,0,1);
            }
            if(null!=oldTxns&&"Cancel Bet".equals(oldTxns.getMethod())){
                return initResponse(s,0,1);
            }
            if(null!=oldTxns&&"Settle".equals(oldTxns.getMethod())){
                return initResponse(s,8,2);
            }
            if(BigDecimal.ZERO.compareTo(money)!=0){
                balance = balance.add(money);
                // 更新余额
                gameCommonService.updateUserBalance(memBaseinfo, money, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            }
            Txns txns = new Txns();
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            txns.setCreateTime(dateStr);
            if(null!=oldTxns){

                BeanUtils.copyProperties(oldTxns, txns);
                oldTxns.setStatus("Cancel Bet");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);
            }
            txns.setWinningAmount(money);
            txns.setWinAmount(money);
            //余额
            txns.setBalance(balance);
            //操作名称
            txns.setMethod("Cancel Bet");
            txns.setStatus("Running");
            // 生成订单数据
            int num = txnsMapper.insert(txns);

            return initResponse(s,0,1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initResponse(s,3,2);
        }
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
     * @return Txns
     */
    private Txns getInitTxns(GameParentPlatform gameParentPlatform, String paySerialno, String playerID,
                             BigDecimal pointAmount, BigDecimal balance, String ip) {
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.V8_PLATFORM_CODE).get(0);
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
        txns.setWinningAmount(pointAmount.negate());
        txns.setWinAmount(pointAmount.negate());
        //创建时间
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        //玩家下注时间
        txns.setBetTime(dateStr);
        //有效投注金额 或 投注面值
        txns.setTurnover(pointAmount);
        //辨认交易时间依据
        txns.setTxTime(dateStr);
        //操作名称
        txns.setMethod("Place Bet");
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
     *  s=1001,1002,1003
     * @return JSONObject
     */
    private JSONObject initResponse(int s,int code,String account,BigDecimal amount) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("m", "/channelHandle");
        jsonObject.put("s", s);
        JSONObject jsonObjectSub = new JSONObject();
        jsonObjectSub.put("code", code);
        jsonObjectSub.put("account", account);
        jsonObjectSub.put("money", amount);
        jsonObject.put("d",jsonObjectSub);
        return jsonObject;
    }

    /**
     * 初始化成功json返回
     *  s=1004,1005
     * @return JSONObject
     */
    private JSONObject initResponse(int s,int code,int status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("m", "/channelHandle");
        jsonObject.put("s", s);
        JSONObject jsonObjectSub = new JSONObject();
        jsonObjectSub.put("code", code);
        jsonObjectSub.put("status", status);
        jsonObject.put("d",jsonObjectSub);
        return jsonObject;
    }

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.V8_PLATFORM_CODE);
    }

    /**
     * 生成请求参数key
     *
     * @param timestamp 13位时间戳
     * @return key
     */
    private String getKey(long timestamp) {
        return MD5Encoder.encode(OpenAPIProperties.V8_AGENT + timestamp + OpenAPIProperties.V8_MD5KEY);
    }

    /**
     * 校验时间戳范围是否在30秒内
     *
     * @param timestamp 请求时间戳
     * @return true false
     */
    private boolean checkTimestamp(long timestamp) {
        return (System.currentTimeMillis() - timestamp) / 1000 > 30;
    }
}
