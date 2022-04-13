package com.indo.game.service.pp.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.game.common.util.PPHashAESEncrypt;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.pp.PpAuthenticateCallBackReq;
import com.indo.game.pojo.dto.pp.PpBalanceCallBackReq;
import com.indo.game.pojo.dto.pp.PpBetCallBackReq;
import com.indo.game.pojo.dto.pp.PpBonusWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpJackpotWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpPromoWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpRefundWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpResultCallBackReq;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.pp.PpCallbackService;
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
 * PP电子回调服务
 */
@Service
public class PpCallbackServiceImpl implements PpCallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private TxnsMapper txnsMapper;

    // 权限校验
    @Override
    public Object authenticate(PpAuthenticateCallBackReq ppAuthenticateCallBackReq, String ip) {
        logger.info("pp_authenticate  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppAuthenticateCallBackReq), ip);

        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppAuthenticateCallBackReq, ppAuthenticateCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法");
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP");
        }
        String ppPlatformCode = OpenAPIProperties.PP_PLATFORM_CODE;
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(ppAuthenticateCallBackReq.getToken(), ppPlatformCode);

        if (null == cptOpenMember) {
            return initFailureResponse(2, "玩家不存在");
        }

        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
        if (null == memBaseinfo) {
            return initFailureResponse(2, "玩家不存在");
        }

        JSONObject json = initSuccessResponse();
        json.put("currency", platformGameParent.getCurrencyType());
        json.put("cash", cptOpenMember.getBalance());
        json.put("bonus", BigDecimal.ZERO);
        json.put("token", cptOpenMember.getPassword());
        return json;
    }

    // 获取余额
    @Override
    public Object getBalance(PpBalanceCallBackReq ppBalanceCallBackReq, String ip) {
        logger.info("pp_getBalance  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppBalanceCallBackReq), ip);

        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppBalanceCallBackReq, ppBalanceCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法");
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP");
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppBalanceCallBackReq.getUserId());
        if (null == memBaseinfo) {
            return initFailureResponse(2, "玩家不存在");
        }

        JSONObject json = initSuccessResponse();
        json.put("currency", platformGameParent.getCurrencyType());
        json.put("cash", memBaseinfo.getBalance());
        json.put("bonus", BigDecimal.ZERO);
        return json;

    }

    // 下注
    @Override
    public Object bet(PpBetCallBackReq ppBetCallBackReq, String ip) {
        logger.info("pp_bet  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppBetCallBackReq), ip);
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppBetCallBackReq, ppBetCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法");
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP");
        }

        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppBetCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在");
            }
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(ppBetCallBackReq.getGameId());
            if (null == gamePlatform) {
                return initFailureResponse(3, "游戏不存在");
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 下注金额
            BigDecimal betAmount = ppBetCallBackReq.getAmount();

            // 查询用户请求订单
            Txns oldTxns = getTxns(ppBetCallBackReq.getReference(), memBaseinfo.getId());
            if (null != oldTxns) {
                return initSuccessResponse(ppBetCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
            }

            if (memBaseinfo.getBalance().compareTo(betAmount) < 0) {
                return initFailureResponse(1, "玩家余额不足");
            }

            // 下注金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                return initFailureResponse(7, "下注金额不能小0");
            }
            balance = balance.subtract(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(ppBetCallBackReq.getReference());
            //此交易是否是投注 true是投注 false 否
            txns.setBet(true);
            //玩家 ID
            txns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
            txns.setGameInfo(ppBetCallBackReq.getRoundDetails());
            txns.setRoundId(ppBetCallBackReq.getRoundId());
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
            txns.setBetAmount(ppBetCallBackReq.getAmount());
            //游戏平台的下注项目
            txns.setBetType(ppBetCallBackReq.getGameId());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
//            txns.setWinningAmount(BigDecimal.ZERO);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(ppBetCallBackReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(ppBetCallBackReq.getAmount());
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(BigDecimal.ZERO);
            //返还金额 (包含下注金额)
            //赌注的结果 : 赢:0,输:1,平手:2
            //有效投注金额 或 投注面值
            txns.setTurnover(ppBetCallBackReq.getAmount());
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(ppBetCallBackReq.getTimestamp(), DateUtils.newFormat));
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
                return initFailureResponse(100, "订单入库请求失败");
            }

            return initSuccessResponse(ppBetCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage());
        }

    }

    // 中奖
    @Override
    public Object result(PpResultCallBackReq ppResultCallBackReq, String ip) {
        logger.info("pp_result  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppResultCallBackReq), ip);
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppResultCallBackReq, ppResultCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法");
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP");
        }
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppResultCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在");
            }
            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 查询用户请求订单
            Txns oldTxns = getTxns(ppResultCallBackReq.getReference(), memBaseinfo.getId());
            if (null != oldTxns) {
                return initSuccessResponse(ppResultCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
            }

            // 中奖金额
            BigDecimal betAmount = ppResultCallBackReq.getAmount();
            // 中奖金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                return initFailureResponse(7, "中奖金额不能小0");
            }

            // 会员余额
            balance = balance.add(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);

            oldTxns = new Txns();
            //游戏商注单号
            oldTxns.setPlatformTxId(ppResultCallBackReq.getReference());
            //玩家 ID
            oldTxns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            oldTxns.setCurrency(platformGameParent.getCurrencyType());
            oldTxns.setGameInfo(ppResultCallBackReq.getRoundDetails());
            oldTxns.setRoundId(ppResultCallBackReq.getRoundId());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            oldTxns.setWinningAmount(ppResultCallBackReq.getAmount());
            oldTxns.setWinAmount(ppResultCallBackReq.getAmount());
            //真实返还金额,游戏赢分
            oldTxns.setRealWinAmount(ppResultCallBackReq.getAmount());
            //辨认交易时间依据
            oldTxns.setTxTime(DateUtils.formatByLong(ppResultCallBackReq.getTimestamp(), DateUtils.newFormat));
            oldTxns.setUpdateTime(DateUtils.formatByLong(ppResultCallBackReq.getTimestamp(), DateUtils.newFormat));
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep;
            if (ppResultCallBackReq.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                resultTyep = 2;
            } else if (ppResultCallBackReq.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                resultTyep = 0;
            } else {
                resultTyep = 1;
            }
            oldTxns.setResultType(resultTyep);
            //操作名称
            oldTxns.setMethod("Settle");
            oldTxns.setStatus("Running");
            //余额
            oldTxns.setBalance(balance);
            //更新时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            oldTxns.setUpdateTime(dateStr);
            //投注 IP
            oldTxns.setBetIp(ip);//  string 是 投注 IP
            int num = txnsMapper.updateById(oldTxns);
            if (num <= 0) {
                return initFailureResponse(100, "订单派奖请求失败");
            }

            return initSuccessResponse(ppResultCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage());
        }

    }

    // 免费回合赢奖
    @Override
    public Object bonusWin(PpBonusWinCallBackReq ppBonusWinCallBackReq, String ip) {
        logger.info("pp_bonuswin  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppBonusWinCallBackReq), ip);
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppBonusWinCallBackReq, ppBonusWinCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法");
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP");
        }
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppBonusWinCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在");
            }// 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(ppBonusWinCallBackReq.getGameId());
            if (null == gamePlatform) {
                return initFailureResponse(3, "游戏不存在");
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            // 赢奖金额
            BigDecimal betAmount = ppBonusWinCallBackReq.getAmount();

            // 赢奖金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                return initFailureResponse(7, "赢奖金额不能小0");
            }

            // 查询用户请求订单
            Txns txns = getTxns(ppBonusWinCallBackReq.getReference(), memBaseinfo.getId());
            if (null != txns) {
                return initSuccessResponse(ppBonusWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
            }
            txns = new Txns();

            // 会员余额
            balance = balance.add(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.FREE_SPIN, TradingEnum.INCOME);

            //游戏商注单号
            txns.setPlatformTxId(ppBonusWinCallBackReq.getReference());
            //此交易是否是投注 true是投注 false 否
            txns.setBet(false);
            //玩家 ID
            txns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
            txns.setGameInfo(ppBonusWinCallBackReq.getGameId());
            txns.setRoundId(ppBonusWinCallBackReq.getRoundId());
            //游戏平台的下注项目
            txns.setBetType(ppBonusWinCallBackReq.getGameId());
            // 奖金游戏
            txns.setHasBonusGame(1);
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
            txns.setWinningAmount(ppBonusWinCallBackReq.getAmount());
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(ppBonusWinCallBackReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(BigDecimal.ZERO);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(ppBonusWinCallBackReq.getAmount());
            //返还金额 (包含下注金额)
            txns.setWinAmount(ppBonusWinCallBackReq.getAmount());
            //赌注的结果 : 赢:0,输:1,平手:2 (零金额会被视为输)
            int resultTyep;
            if (ppBonusWinCallBackReq.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                resultTyep = 0;
            } else {
                resultTyep = 1;
            }
            txns.setResultType(resultTyep);
            //有效投注金额 或 投注面值
            txns.setTurnover(BigDecimal.ZERO);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(ppBonusWinCallBackReq.getTimestamp(), DateUtils.newFormat));
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);
            //创建时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP
            txns.setCreateTime(dateStr);
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(100, "订单入库请求失败");
            }

            return initSuccessResponse(ppBonusWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage());
        }

    }

    // 累计奖金赢奖(促销)
    @Override
    public Object jackpotWin(PpJackpotWinCallBackReq ppJackpotWinCallBackReq, String ip) {
        logger.info("pp_jackpotwin  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppJackpotWinCallBackReq), ip);
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppJackpotWinCallBackReq, ppJackpotWinCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法");
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP");
        }
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppJackpotWinCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在");
            }
            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(ppJackpotWinCallBackReq.getGameId());
            if (null == gamePlatform) {
                return initFailureResponse(3, "游戏不存在");
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            // 赢奖金额
            BigDecimal betAmount = ppJackpotWinCallBackReq.getAmount();

            // 赢奖金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                return initFailureResponse(7, "赢奖金额不能小0");
            }

            // 查询用户请求订单
            Txns txns = getTxns(ppJackpotWinCallBackReq.getReference(), memBaseinfo.getId());
            if (null != txns) {
                return initSuccessResponse(ppJackpotWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
            }
            txns = new Txns();
            // 会员余额
            balance = balance.add(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.reward, TradingEnum.INCOME);

            //游戏商注单号
            txns.setPlatformTxId(ppJackpotWinCallBackReq.getReference());
            //此交易是否是投注 true是投注 false 否
            txns.setBet(false);
            //玩家 ID
            txns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
            txns.setGameInfo(ppJackpotWinCallBackReq.getGameId());
            txns.setRoundId(ppJackpotWinCallBackReq.getRoundId());
            //游戏平台的下注项目
            txns.setBetType(ppJackpotWinCallBackReq.getJackpotId());
            // 奖金游戏
            txns.setHasBonusGame(1);
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
            txns.setWinningAmount(ppJackpotWinCallBackReq.getAmount());
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(ppJackpotWinCallBackReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(BigDecimal.ZERO);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(ppJackpotWinCallBackReq.getAmount());
            //返还金额 (包含下注金额)
            txns.setWinAmount(ppJackpotWinCallBackReq.getAmount());
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep;
            if (ppJackpotWinCallBackReq.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                resultTyep = 2;
            } else if (ppJackpotWinCallBackReq.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                resultTyep = 0;
            } else {
                resultTyep = 1;
            }
            txns.setResultType(resultTyep);
            //有效投注金额 或 投注面值
            txns.setTurnover(BigDecimal.ZERO);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(ppJackpotWinCallBackReq.getTimestamp(), DateUtils.newFormat));
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);
            //创建时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP
            txns.setCreateTime(dateStr);
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(100, "订单入库请求失败");
            }

            return initSuccessResponse(ppJackpotWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage());
        }

    }

    // 活动奖励
    @Override
    public Object promoWin(PpPromoWinCallBackReq ppPromoWinCallBackReq, String ip) {
        logger.info("pp_promowin  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppPromoWinCallBackReq), ip);
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppPromoWinCallBackReq, ppPromoWinCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法");
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP");
        }
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppPromoWinCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在");
            }
            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.PP_PLATFORM_CODE).get(0);
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            // 赢奖金额
            BigDecimal betAmount = ppPromoWinCallBackReq.getAmount();

            // 赢奖金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                return initFailureResponse(7, "赢奖金额不能小0");
            }

            // 查询用户请求订单
            Txns txns = getTxns(ppPromoWinCallBackReq.getReference(), memBaseinfo.getId());
            if (null != txns) {
                return initSuccessResponse(ppPromoWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
            }
            txns = new Txns();
            // 会员余额
            balance = balance.add(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);

            //游戏商注单号
            txns.setPlatformTxId(ppPromoWinCallBackReq.getReference());
            //此交易是否是投注 true是投注 false 否
            txns.setBet(false);
            //玩家 ID
            txns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            txns.setCurrency(ppPromoWinCallBackReq.getCurrency());
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
            txns.setWinningAmount(ppPromoWinCallBackReq.getAmount());
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(ppPromoWinCallBackReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(BigDecimal.ZERO);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(ppPromoWinCallBackReq.getAmount());
            //返还金额 (包含下注金额)
            txns.setWinAmount(ppPromoWinCallBackReq.getAmount());
            // 活动派彩
            txns.setAmount(ppPromoWinCallBackReq.getAmount());
            // 活动ID
            txns.setPromotionId(ppPromoWinCallBackReq.getCampaignId());
            // 活动类型ID
            txns.setPromotionTypeId(ppPromoWinCallBackReq.getCampaignType());
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep;
            if (ppPromoWinCallBackReq.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                resultTyep = 2;
            } else if (ppPromoWinCallBackReq.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                resultTyep = 0;
            } else {
                resultTyep = 1;
            }
            txns.setResultType(resultTyep);
            //有效投注金额 或 投注面值
            txns.setTurnover(BigDecimal.ZERO);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(ppPromoWinCallBackReq.getTimestamp(), DateUtils.newFormat));
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
                return initFailureResponse(100, "活动派奖订单入库请求失败");
            }

            return initSuccessResponse(ppPromoWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage());
        }
    }

    // 退款
    @Override
    public Object refund(PpRefundWinCallBackReq ppRefundWinCallBackReq, String ip) {
        logger.info("pp_refund  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppRefundWinCallBackReq), ip);
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppRefundWinCallBackReq, ppRefundWinCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法");
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP");
        }

        try {

            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppRefundWinCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在");
            }
            // 构建返回
            JSONObject json = initSuccessResponse();

            // 查询用户请求订单
            Txns oldTxns = getTxns(ppRefundWinCallBackReq.getReference(), memBaseinfo.getId());
            if (null == oldTxns) {
                return json;
            }

            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                json.put("cash", memBaseinfo.getBalance());
                json.put("transactionId", ppRefundWinCallBackReq.getReference());
                return json;
            }
            BigDecimal realWinAmount = ppRefundWinCallBackReq.getAmount();
            if (null == realWinAmount) {
                realWinAmount = oldTxns.getBetAmount();
            }
            BigDecimal balance = memBaseinfo.getBalance().add(oldTxns.getAmount());
            gameCommonService.updateUserBalance(memBaseinfo, realWinAmount, GoldchangeEnum.REFUND, TradingEnum.INCOME);
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            txns.setBalance(balance);
            txns.setId(null);
            txns.setStatus("Running");
            txns.setRealWinAmount(realWinAmount);//真实返还金额
            txns.setMethod("Adjust Bet");
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);

            oldTxns.setStatus("Adjust");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);

            json.put("transactionId", ppRefundWinCallBackReq.getReference());
            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage());
        }
    }


    /**
     * 判断请求参数hash是否合法
     *
     * @param object  请求参数对象
     * @param reqHash 请求hash
     * @return boolean
     */
    private boolean checkReqHash(Object object, String reqHash) {
        // 根据请求参数生成HASH
        String reqParamHash = PPHashAESEncrypt.encrypt(object, OpenAPIProperties.PP_API_SECRET_KEY);
        // 与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        return StringUtils.isBlank(reqHash) || !reqHash.equals(reqParamHash);
    }

    /**
     * 获取历史订单
     *
     * @param reference 订单编号
     * @param userId    会员ID
     * @return 订单
     */
    private Txns getTxns(String reference, Long userId) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, reference);
        wrapper.eq(Txns::getUserId, userId);
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 初始化成功json返回
     *
     * @return JSONObject
     */
    private JSONObject initSuccessResponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", 0);
        jsonObject.put("description", "Success");
        return jsonObject;
    }

    /**
     * 初始化成功json返回
     *
     * @return JSONObject
     */
    private JSONObject initSuccessResponse(String transactionId, String currency, BigDecimal balance) {
        JSONObject json = initSuccessResponse();
        json.put("transactionId", transactionId);
        json.put("currency", currency);
        json.put("cash", balance);
        json.put("bonus", BigDecimal.ZERO);
        json.put("usedPromo", BigDecimal.ZERO);
        return json;
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
        jsonObject.put("error", error);
        jsonObject.put("description", description);
        return jsonObject;
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
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.PP_PLATFORM_CODE);
    }
}
