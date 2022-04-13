package com.indo.game.service.v8.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.encrypt.MD5Encoder;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.v8.V8CallbackService;
import com.indo.user.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public Object getBalance(String agent, String timestamp, String account, String key, String ip) {
        logger.info("v8_getBalance v8Game paramJson:{},{},{},{}, ip:{}", agent, timestamp, account, key, ip);
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(28, "ip 被禁用");
            }

            // 渠道不存在（请检查渠道 ID 是否正确）
            if (!OpenAPIProperties.V8_AGENT.equals(agent)) {
                return initFailureResponse(2, "渠道不存在");
            }

            // 渠道验证错误
            if (!key.equals(getKey(Long.parseLong(account)))) {
                return initFailureResponse(15, "key 验证失败");
            }

            // 验证时间超时 , 大于30秒
            if (checkTimestamp(Long.parseLong(timestamp))) {
                return initFailureResponse(3, "验证时间超时");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
            if (null == memBaseinfo) {
                return initFailureResponse(1024, "会员不存在");
            }

            // 会员余额返回
            if (memBaseinfo.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                JSONObject jsonObject1 = initSuccessResponse();
                jsonObject1.put("money", memBaseinfo.getBalance());
                return jsonObject1;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // 数据库异常
        return initFailureResponse(27, "会员余额不足");
    }

    @Override
    public Object debit(String agent, String timestamp, String account, String key, BigDecimal money, String ip) {
        logger.info("v8_debit v8Game paramJson:{},{},{},{},{}, ip:{}", agent, timestamp, account, key, money, ip);

        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(28, "ip 被禁用");
            }

            // 渠道不存在（请检查渠道 ID 是否正确）
            if (!OpenAPIProperties.V8_AGENT.equals(agent)) {
                return initFailureResponse(2, "渠道不存在");
            }

            // 渠道验证错误
            if (!key.equals(getKey(Long.parseLong(account)))) {
                return initFailureResponse(15, "key 验证失败");
            }

            // 验证时间超时 , 大于30秒
            if (checkTimestamp(Long.parseLong(timestamp))) {
                return initFailureResponse(3, "验证时间超时");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
            if (null == memBaseinfo) {
                return initFailureResponse(1024, "会员不存在");
            }

            BigDecimal balance = memBaseinfo.getBalance();

            if (memBaseinfo.getBalance().compareTo(balance) < 0) {
                return initFailureResponse(1005, "玩家余额不足");
            }

            String paySerialno = GeneratorIdUtil.generateId();
            // 生成订单数据
            Txns txns = getInitTxns(platformGameParent, paySerialno, account, money, balance, ip);
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(1006, "资料库异常");
            }

            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, money, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
            return initSuccessResponse();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return initFailureResponse(999, "资料库异常");
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
        //创建时间
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        //玩家下注时间
        txns.setBetTime(dateStr);
        //有效投注金额 或 投注面值
        txns.setTurnover(pointAmount);
        //辨认交易时间依据
        txns.setTxTime(dateStr);
        //操作名称
        txns.setMethod("Settle");
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
        jsonObject.put("code", 0);
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
        jsonObject.put("code", error);
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
