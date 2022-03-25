package com.indo.game.service.rich.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.game.common.util.RichSHAEncrypt;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.rich.Rich88ActivityReq;
import com.indo.game.pojo.dto.rich.Rich88TransferReq;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.rich.Rich88CallbackService;
import com.indo.user.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


@Service
public class Rich88CallbackServiceImpl implements Rich88CallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object getSessionId(String apiKey, String pfId, String timestamp, String ip) {
        logger.info("rich88_getSessionId rich88Game paramJson:{}, ip:{}", pfId, ip);
        GameParentPlatform gameParentPlatform = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, gameParentPlatform)) {
            return initFailureResponse(16005, "非信任來源IP");
        }

        // 校验API key
        if (StringUtils.isBlank(pfId) || !pfId.equals(OpenAPIProperties.RICH_PF_ID)) {
            return initFailureResponse(20008, "PF_ID参数错误");
        }

        // 校验API key
        if (StringUtils.isBlank(apiKey) || !apiKey.equals(getApiKey(pfId, timestamp))) {
            return initFailureResponse(16007, "API key 驗證錯誤");
        }

        JSONObject jsonObject = initSuccessResponse();
        JSONObject data = new JSONObject();
        data.put("sid", OpenAPIProperties.RICH_SESSION_ID);
        jsonObject.put("data", data);
        return jsonObject;
    }

    @Override
    public Object getBalance(String authorization, String account, String ip) {
        logger.info("rich88_getBalance rich88Game paramJson:{}, ip:{}", account, ip);
        GameParentPlatform gameParentPlatform = getGameParentPlatform();

        // 校验IP
        if (checkIp(ip, gameParentPlatform)) {
            return initFailureResponse(16005, "非信任來源IP");
        }

        // 验证sessionid
        if (!authorization.equals(OpenAPIProperties.RICH_SESSION_ID)) {
            return initFailureResponse(22003, "Token 驗證錯誤");
        }

        // 查询玩家是否存在
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
        if (null == memBaseinfo) {
            return initFailureResponse(13002, "帳號不存在");
        }

        JSONObject jsonObject = initSuccessResponse();
        JSONObject data = new JSONObject();
        data.put("balance", memBaseinfo.getBalance());
        jsonObject.put("data", data);
        return jsonObject;
    }

    @Override
    public Object transfer(Rich88TransferReq rich88TransferReq, String authorization, String ip) {
        logger.info("rich88_transfer rich88Game paramJson:{}, ip:{}", JSONObject.toJSONString(rich88TransferReq), ip);
        GameParentPlatform gameParentPlatform = getGameParentPlatform();

        // 校验IP
        if (checkIp(ip, gameParentPlatform)) {
            return initFailureResponse(16005, "非信任來源IP");
        }

        // 验证sessionid
        if (!authorization.equals(OpenAPIProperties.RICH_SESSION_ID)) {
            return initFailureResponse(22003, "Token 驗證錯誤");
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(rich88TransferReq.getAccount());
        if (null == memBaseinfo) {
            return initFailureResponse(13002, "帳號不存在");
        }
        // 交易序号
        String paySerialno = StringUtils.isNotEmpty(rich88TransferReq.getTransfer_no())
                ? rich88TransferReq.getTransfer_no()
                : rich88TransferReq.getRecord_id();
        if (StringUtils.isBlank(paySerialno)) {
            return initFailureResponse(20008, "交易序号不能为空");
        }
        try {
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(rich88TransferReq.getGame_code());

            // 查询订单
            Txns oldTxns = getTxns(gameParentPlatform, paySerialno, rich88TransferReq.getAccount());

            BigDecimal balance = memBaseinfo.getBalance();
            BigDecimal amount = rich88TransferReq.getMoney();

            String txnsMethod;
            // 提款：withdraw
            if ("withdraw".equals(rich88TransferReq.getAction())) {
                // 重复订单
                if (null != oldTxns) {
                    return initFailureResponse(20008, "提款交易重复");
                }
                if (memBaseinfo.getBalance().compareTo(amount) < 0) {
                    return initFailureResponse(22007, "單⼀錢包玩家⾦錢不⾜");
                }
                balance = balance.subtract(amount);
                txnsMethod = "Place Bet";
                // 更新余额
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
            } else if ("deposit".equals(rich88TransferReq.getAction())) {   // 存款
                // 重复订单
                if (null != oldTxns) {
                    return initFailureResponse(20008, "存款交易重复");
                }
                balance = balance.add(amount);
                txnsMethod = "Settle";
                // 更新余额
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
            } else {   // rollback 取消提款
                // 取消提款订单不存在
                if (null == oldTxns) {
                    return initFailureResponse(22005, "單⼀錢包交易記錄不存在");
                }
                // 比对金额和提款订单金额要一致
                if (oldTxns.getAmount().compareTo(amount) != 0) {
                    return initFailureResponse(20008, "取消提款不正确");
                }

                balance = balance.add(amount);
                txnsMethod = "Cancel Bet";
                // 取消订单号需要重新生成
                paySerialno = GoldchangeEnum.DSFYXZZ.name() + GeneratorIdUtil.generateId();
                // 更新余额
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
            }

            // 生成订单数据
            Txns txns = getInitTxns(gamePlatform, gameParentPlatform, paySerialno,
                    rich88TransferReq.getAccount(), amount, balance, ip, txnsMethod);
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(22008, "單⼀錢包平台發⽣錯誤");
            }

            if ("rollback".equals(rich88TransferReq.getAction()) && null != oldTxns) {
                // 更新提款订单
                oldTxns.setStatus("Cancel");
                oldTxns.setUpdateTime(DateUtils.format(new Date(), DateUtils.newFormat));
                txnsMapper.updateById(oldTxns);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return initSuccessResponse();
    }

    @Override
    public Object awardActivity(Rich88ActivityReq rich88ActivityReq, String authorization, String ip) {
        logger.info("rich88_awardActivity rich88Game paramJson:{}, ip:{}", JSONObject.toJSONString(rich88ActivityReq), ip);
        GameParentPlatform platformGameParent = getGameParentPlatform();

        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(16005, "非信任來源IP");
        }

        // 验证sessionid
        if (!authorization.equals(OpenAPIProperties.RICH_SESSION_ID)) {
            return initFailureResponse(22003, "Token 驗證錯誤");
        }

        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(rich88ActivityReq.getAccount());
        if (null == memBaseinfo) {
            return initFailureResponse(13002, "帳號不存在");
        }

        try {

            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(OpenAPIProperties.RICH_PLATFORM_CODE);
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            // 赢奖金额
            BigDecimal betAmount = rich88ActivityReq.getMoney();

            // 赢奖金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) <= 0) {
                return initFailureResponse(20007, "奖励金额小于等于0");
            }

            // 查询用户请求订单
            Txns txns = getActivityTxns(platformGameParent, rich88ActivityReq.getAward_id(), memBaseinfo.getId().toString());
            if (null != txns) {
                return initFailureResponse(20006, "獎勵已送出");
            }

            txns = new Txns();
            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance().add(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);

            //游戏商注单号
            txns.setPlatformTxId(rich88ActivityReq.getAward_id());
            //此交易是否是投注 true是投注 false 否
            txns.setBet(false);
            //玩家 ID
            txns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            txns.setCurrency(rich88ActivityReq.getCurrency());
            //游戏平台的下注项目
//            txns.setBetType(ppPromoWinCallBackReq.getProviderId());
            // 奖金游戏
//            txns.setHasBonusGame(1);
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
            txns.setBetAmount(BigDecimal.ZERO);
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(rich88ActivityReq.getMoney());
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(System.currentTimeMillis(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(BigDecimal.ZERO);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(rich88ActivityReq.getMoney());
            //返还金额 (包含下注金额)
            txns.setWinAmount(rich88ActivityReq.getMoney());
            // 活动派彩
            txns.setAmount(rich88ActivityReq.getMoney());
            // 活动ID
            txns.setPromotionId(rich88ActivityReq.getEvent_id());
            // 活动类型ID
            txns.setPromotionTypeId(rich88ActivityReq.getActivity_type());
            //有效投注金额 或 投注面值
            txns.setTurnover(BigDecimal.ZERO);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(System.currentTimeMillis(), DateUtils.newFormat));
            //操作名称
            txns.setMethod("Settle");
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
                return initFailureResponse(10002, "活动派奖订单入库请求失败");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(10010, e.getMessage());
        }

        return initSuccessResponse();
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
     * 获取请求 header api key
     *
     * @param pfId      pfId
     * @param timestamp timestamp
     * @return String
     */
    private String getApiKey(String pfId, String timestamp) {
        String checkValue = pfId + OpenAPIProperties.RICH_PRIVATE_KEY + timestamp;
        return RichSHAEncrypt.getSHA256Str(checkValue);
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
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        if (StringUtils.isNoneBlank(playerID)) {
            wrapper.eq(Txns::getUserId, playerID);
        }
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 查询第三方活动派奖订单是否存在
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @param playerID           playerID
     * @return Txns
     */
    private Txns getActivityTxns(GameParentPlatform gameParentPlatform, String paySerialno, String playerID) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Settle"));
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
     * @param gamePlatform       gamePlatform
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @param playerID           playerID
     * @param pointAmount        pointAmount
     * @param balance            balance
     * @param ip                 ip
     * @param method             method
     * @return Txns
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
     * 初始化成功json返回
     *
     * @return JSONObject
     */
    private JSONObject initSuccessResponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "Success");
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
        jsonObject.put("msg", description);
        return jsonObject;
    }

    private GameParentPlatform getGameParentPlatform(){
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.RICH_PLATFORM_CODE);
    }
}
