package com.indo.game.service.wm.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.wm.WmCallbackService;
import com.indo.user.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class WmCallbackServiceImpl implements WmCallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;


    @Override
    public Object getBalance(JSONObject params, String ip) {
        logger.info("wm_getBalance wmGame paramJson:{}, ip:{}", params, ip);
        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(900, "非信任來源IP");
            }

            if (!params.containsKey("signature") || StringUtils.isBlank(params.getString("signature"))) {
                return initFailureResponse(10304, "代理商代码(signature)为空");
            }

            if (!OpenAPIProperties.WM_SIGNATURE.equals(params.getString("signature"))) {
                return initFailureResponse(10303, "有此代理商ID,但代理商代码(signature)错误");
            }

            if (!params.containsKey("user") || StringUtils.isBlank(params.getString("user"))) {
                return initFailureResponse(10502, "帐号名不得为空");
            }

            String account = params.getString("user");
            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
            if (null == memBaseinfo) {
                return initFailureResponse(10501, "查无此帐号,请检查");
            }
            JSONObject respJson = new JSONObject();
            respJson.put("user", memBaseinfo.getAccount());
            respJson.put("money", memBaseinfo.getBalance());

            return initSuccessResponse(respJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(911, e.getMessage());
        }
    }

    @Override
    public Object pointInout(JSONObject params, String ip) {
        logger.info("wm_pointInout wmGame paramJson:{}, ip:{}", params, ip);

        try {
            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(900, "非信任來源IP");
            }

            if (!params.containsKey("signature") || StringUtils.isBlank(params.getString("signature"))) {
                return initFailureResponse(10304, "代理商代码(signature)为空");
            }

            if (!OpenAPIProperties.WM_SIGNATURE.equals(params.getString("signature"))) {
                return initFailureResponse(10303, "有此代理商ID,但代理商代码(signature)错误");
            }

            if (!params.containsKey("user") || StringUtils.isBlank(params.getString("user"))) {
                return initFailureResponse(10502, "帐号名不得为空");
            }

            String userName = params.getString("user");
            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(10501, "查无此帐号,请检查");
            }

            String paySerialno = params.getString("dealid");
            String dateSent = params.getString("requestDate");
            BigDecimal betAmount = params.getBigDecimal("money");
            // 游戏项目_期数_局号_加扣点类型 例:101_112139999_88_2
            String type = params.getString("type");
            String gtype = params.getString("gtype");
            // 加扣点类型 0:电子游戏结算 1:加点 2:扣点 3:重对加点 4:重对扣点 5:重新派彩 cancel:取消
            String code = params.getString("code");
            GoldchangeEnum goldchangeEnum = GoldchangeEnum.PLACE_BET;
            TradingEnum tradingEnum = TradingEnum.SPENDING;
            String method = "Place Bet";
            GamePlatform gamePlatform;
            if(OpenAPIProperties.WM_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.WM_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
            }else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCode(gtype);
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            Txns oldTxns = getTxns(gameParentPlatform, paySerialno);
            BigDecimal balance = memBaseinfo.getBalance();
            // 扣钱
            if (code.equals("2") || code.equals("4")) {
                if (balance.compareTo(betAmount) < 0) {
                    return initFailureResponse(10805, "转账失败，该账号余额不足");
                }
                // 重复订单
                if (null != oldTxns) {
                    return initFailureResponse(900, "该注单已承认");
                }
                balance = balance.subtract(betAmount.abs());

                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(paySerialno);

                //玩家 ID
                txns.setUserId(memBaseinfo.getAccount());
                //玩家货币代码
                txns.setCurrency(gameParentPlatform.getCurrencyType());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
//            txns.setRoundId(wmCallbackBetReq.getRound());
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
                txns.setBetAmount(betAmount);
                //游戏平台的下注项目
                txns.setBetType(type);
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount);
                //玩家下注时间
                txns.setBetTime(DateUtils.formatByString(dateSent, DateUtils.newFormat));
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(betAmount);
                //真实返还金额,游戏赢分
//            txns.setRealWinAmount(winloseAmount);
                //此交易是否是投注 true是投注 false 否
                txns.setBet(true);
                //返还金额 (包含下注金额)
//            txns.setWinAmount(winloseAmount);
                //有效投注金额 或 投注面值
                txns.setTurnover(betAmount);
                //辨认交易时间依据
                txns.setTxTime(DateUtils.formatByString(dateSent, DateUtils.newFormat));
                //操作名称
                txns.setMethod(method);
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
                    return initFailureResponse(900, "订单写入失败");
                }

            } else if (code.equals("1") || code.equals("3")) {
                // 缺少下注订单
                if (null == oldTxns) {
                    return initFailureResponse(900, "该注单不存在");
                }

                if ("Adjust Bet".equals(oldTxns.getMethod())) {
                    return initFailureResponse(900, "该注单已经取消");
                }
                goldchangeEnum = GoldchangeEnum.SETTLE;
                tradingEnum = TradingEnum.INCOME;
                balance = balance.add(betAmount.abs());

                //真实返还金额,游戏赢分
                oldTxns.setRealWinAmount(betAmount);
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                oldTxns.setWinningAmount(betAmount);
                //返还金额 (包含下注金额)
                oldTxns.setWinAmount(betAmount);
                //赌注的结果 : 赢:0,输:1,平手:2
                oldTxns.setResultType(1);
                //操作名称
                oldTxns.setMethod(method);
                oldTxns.setStatus("Running");
                oldTxns.setUpdateTime(DateUtils.format(new Date(), DateUtils.newFormat));
                int num = txnsMapper.updateById(oldTxns);
                if (num <= 0) {
                    return initFailureResponse(900, "订单写入失败");
                }
            }

            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, goldchangeEnum, tradingEnum);

            JSONObject respJson = new JSONObject();
            respJson.put("money", betAmount);
            respJson.put("cash", balance);
            respJson.put("dealid", paySerialno);

            return initSuccessResponse(respJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(911, e.getMessage());
        }
    }

    // 已下注的取消下注, 未派彩的重新派彩, 其他场景不处理
    @Override
    public Object timeoutBetReturn(JSONObject params, String ip) {
        logger.info("wm_timeoutBetReturn wmGame paramJson:{}, ip:{}", params, ip);

        try {
            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(900, "非信任來源IP");
            }

            if (!params.containsKey("signature") || StringUtils.isBlank(params.getString("signature"))) {
                return initFailureResponse(10304, "代理商代码(signature)为空");
            }

            if (!OpenAPIProperties.WM_SIGNATURE.equals(params.getString("signature"))) {
                return initFailureResponse(10303, "有此代理商ID,但代理商代码(signature)错误");
            }

            if (!params.containsKey("user") || StringUtils.isBlank(params.getString("user"))) {
                return initFailureResponse(10502, "帐号名不得为空");
            }

            String userName = params.getString("user");
            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(10501, "查无此帐号,请检查");
            }

            String paySerialno = params.getString("dealid");
            String dateSent = params.getString("requestDate");
            BigDecimal betAmount = params.getBigDecimal("money");
            // 游戏项目_期数_局号_加扣点类型 例:101_112139999_88_2
            String type = params.getString("type");
            String gtype = params.getString("gtype");
            // 加扣点类型 :加点 2:扣点
            Integer code = params.getInteger("code");
            GoldchangeEnum goldchangeEnum;
            BigDecimal balance = memBaseinfo.getBalance().add(betAmount.abs());

            Txns oldTxns = getTxns(gameParentPlatform, paySerialno);
            // 下注回滚
            if (code.equals(2)) {
                // 订单不存在
                if (null == oldTxns) {
                    return initFailureResponse(900, "该注单不存在");
                }
                if (oldTxns.getBetAmount().compareTo(betAmount) != 0) {
                    return initFailureResponse(900, "取消金额和下注金额不一致");
                }

                goldchangeEnum = GoldchangeEnum.CANCEL_BET;

                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setBalance(balance);
                txns.setId(null);
                txns.setStatus("Running");
                txns.setRealWinAmount(betAmount.abs());//真实返还金额
                txns.setMethod("Adjust Bet");
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);

                oldTxns.setStatus("Adjust");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);


            } else if (code.equals(1)) {
                // 派彩回滚
                if (null != oldTxns) {
                    return initFailureResponse(900, "该注单已派彩");
                }
                goldchangeEnum = GoldchangeEnum.SETTLE;

                GamePlatform gamePlatform;
                if(OpenAPIProperties.WM_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
                    gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.WM_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
                }else {
                    gamePlatform = gameCommonService.getGamePlatformByplatformCode(gtype);
                }
                GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(paySerialno);

                //玩家 ID
                txns.setUserId(memBaseinfo.getAccount());
                //玩家货币代码
                txns.setCurrency(gameParentPlatform.getCurrencyType());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
//            txns.setRoundId(wmCallbackBetReq.getRound());
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
                txns.setBetAmount(betAmount);
                //游戏平台的下注项目
                txns.setBetType(type);
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(betAmount);
                //玩家下注时间
                txns.setBetTime(DateUtils.formatByString(dateSent, DateUtils.newFormat));
                //真实下注金额,需增加在玩家的金额
//                txns.setRealBetAmount(betAmount);
                //真实返还金额,游戏赢分
                txns.setRealWinAmount(betAmount);
                //赌注的结果 : 赢:0,输:1,平手:2
                txns.setResultType(1);
                //此交易是否是投注 true是投注 false 否
                txns.setBet(false);
                //返还金额 (包含下注金额)
                txns.setWinAmount(betAmount);
                //有效投注金额 或 投注面值
//                txns.setTurnover(betAmount);
                //辨认交易时间依据
                txns.setTxTime(DateUtils.formatByString(dateSent, DateUtils.newFormat));
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
                    return initFailureResponse(900, "订单写入失败");
                }
            } else {
                return initFailureResponse(900, "加扣点类型不正确");
            }

            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, goldchangeEnum, TradingEnum.INCOME);

            JSONObject respJson = new JSONObject();
            respJson.put("money", betAmount);
            respJson.put("cash", balance);
            respJson.put("dealid", paySerialno);

            return initSuccessResponse(respJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(911, e.getMessage());
        }
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
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
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
    private JSONObject initSuccessResponse(JSONObject resp) {
        resp.put("responseDate", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", 0);
        jsonObject.put("result", resp);
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
        jsonObject.put("errorCode", error);
        jsonObject.put("errorMessage", description);
        return jsonObject;
    }

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.WM_PLATFORM_CODE);
    }

}
