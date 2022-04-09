package com.indo.game.service.bti.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.bti.BtiBetRequest;
import com.indo.game.pojo.dto.bti.BtiCreditPurchaseRequest;
import com.indo.game.pojo.dto.bti.BtiCreditRequest;
import com.indo.game.pojo.dto.bti.BtiCreditSelectionRequest;
import com.indo.game.pojo.dto.bti.BtiReserveBetsRequest;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.bti.BtiCallbackService;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.user.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BtiCallbackServiceImpl implements BtiCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object validateToken(String authToken, String ip) {
        logger.info("bti_validateToken btiGame paramJson:{}, ip:{}", authToken, ip);
        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(-6, "非信任來源IP");
            }

            CptOpenMember cptOpenMember = externalService.quertCptOpenMember(authToken, gameParentPlatform.getPlatformCode());

            if (null == cptOpenMember) {
                return initFailureResponse(-3, "token 错误");
            }

            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
            if (null == memBaseinfo) {
                return initFailureResponse(-2, "用户不存在");
            }

            JSONObject respJson = initSuccessResponse();
            respJson.put("cust_id", memBaseinfo.getAccount());
            respJson.put("cust_login", memBaseinfo.getAccount());
            respJson.put("city", "");
            respJson.put("country", "");
            respJson.put("currency_code", gameParentPlatform.getCurrencyType());
            respJson.put("balance", memBaseinfo.getBalance());

            return respJson;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(-1, e.getMessage());
        }
    }

    @Override
    public Object reserve(BtiReserveBetsRequest reserveBetsRequest, String ip) {
        logger.info("bti_reserve btiGame paramJson:{}, ip:{}", JSONObject.toJSONString(reserveBetsRequest), ip);

        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(-6, "非信任來源IP");
            }

            String paySerialno = reserveBetsRequest.getReserveId();
            String userName = reserveBetsRequest.getCustId();
            BigDecimal betAmount = BigDecimal.valueOf(reserveBetsRequest.getBetList().stream().mapToDouble(o -> o.getStake().doubleValue()).sum());


            // 查询订单
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(-2, "用户不存在");
            }
            BigDecimal balance = memBaseinfo.getBalance();
            if (balance.compareTo(betAmount) < 0) {
                return initFailureResponse(-95, "玩家余额不足");
            }

            Txns oldTxns = getTxns(gameParentPlatform, paySerialno);
            // 重复订单
            if (null != oldTxns) {
                return initFailureResponse(-96, "该注单已承认");
            }
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(reserveBetsRequest.getPlatform());
            if (null == gamePlatform) {
                return initFailureResponse(-96, "游戏不存在");
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            balance = balance.subtract(betAmount);
            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);


            List<BtiBetRequest> list = reserveBetsRequest.getBetList();
            for (BtiBetRequest item : list) {
                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(paySerialno);
                txns.setRePlatformTxId(item.getBetID());

                //玩家 ID
                txns.setUserId(memBaseinfo.getId().toString());
                //玩家货币代码
                txns.setCurrency(gameParentPlatform.getCurrencyType());
                txns.setOdds(BigDecimal.valueOf(item.getOdds()));

                txns.setRoundId(item.getPurchaseBetID());
                // lineid拼接写入游戏信息中
                txns.setGameInfo(item.getLineList().stream().map(a -> a.getLineID()).collect(Collectors.joining(",")));
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
                txns.setBetAmount(item.getStake());
                //游戏平台的下注项目
                txns.setBetType(item.getBetTypeID());
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
//            txns.setWinningAmount(winloseAmount);
                //玩家下注时间
                txns.setBetTime(DateUtils.format(item.getCreationDate(), DateUtils.newFormat));
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(item.getStake());
                //真实返还金额,游戏赢分
//            txns.setRealWinAmount(winloseAmount);
                //此交易是否是投注 true是投注 false 否
                txns.setBet(true);
                //返还金额 (包含下注金额)
//            txns.setWinAmount(winloseAmount);
                //有效投注金额 或 投注面值
                txns.setTurnover(item.getStake());
                //辨认交易时间依据
                txns.setTxTime(DateUtils.format(item.getCreationDate(), DateUtils.newFormat));
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
                    return initFailureResponse(-1, "订单写入失败");
                }

            }

            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("trx_id", paySerialno);
            jsonObject.put("balance", balance);
            jsonObject.put("BonusUsed", BigDecimal.ZERO);
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(-1, e.getMessage());
        }
    }

    @Override
    public Object debitReserve(BtiReserveBetsRequest reserveBetsRequest, String ip) {
        logger.info("bti_debitReserve btiGame paramJson:{}, ip:{}", JSONObject.toJSONString(reserveBetsRequest), ip);

        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(0, "非信任來源IP");
            }

            String paySerialno = reserveBetsRequest.getReserveId();
            String userName = reserveBetsRequest.getCustId();

            // 查询订单
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(0, "用户不存在");
            }
            BigDecimal balance = memBaseinfo.getBalance();
            // reserve_id 查询是否存在
            Txns oldTxnsReserve = getTxns(gameParentPlatform, paySerialno);
            if (null == oldTxnsReserve) {
                return initFailureResponse(0, "reserve_id not found");
            }

            // 预扣订单下数据
            List<Txns> txnslist = getTxnsList(gameParentPlatform, paySerialno);
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            List<BtiBetRequest> list = reserveBetsRequest.getBetList();
            for (BtiBetRequest item : list) {
                for (Txns txns : txnslist) {
                    if (item.getBetID().equals(txns.getRePlatformTxId())) {
                        txns.setOdds(BigDecimal.valueOf(item.getOdds()));
                        txns.setStatus("Settle");
                        txns.setUpdateTime(dateStr);
                        txnsMapper.updateById(txns);
                    }
                }

            }
            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("trx_id", paySerialno);
            jsonObject.put("balance", balance);
            jsonObject.put("BonusUsed", BigDecimal.ZERO);
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(0, e.getMessage());
        }
    }

    @Override
    public Object cancelReserve(BtiReserveBetsRequest reserveBetsRequest, String ip) {
        logger.info("bti_refund  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(reserveBetsRequest), ip);

        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(-6, "非信任來源IP");
            }

            String paySerialno = reserveBetsRequest.getReserveId();
            String userName = reserveBetsRequest.getCustId();
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(-2, "用户不存在");
            }

            // 查询用户请求订单
            Txns oldTxns = getTxns(platformGameParent, paySerialno);
            if (null == oldTxns) {
                JSONObject json = initSuccessResponse();
                json.put("error_message", "Reserve was not found");
                json.put("balance", memBaseinfo.getBalance());
                return json;
            }

            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                return initFailureResponse(5, "注单已取消");
            }

            // 如果订单有已经结算的
            if (null != getTxns4Re(platformGameParent, paySerialno)) {
                return initFailureResponse(5, "注单已结算");
            }

            // 回退金额（预扣款注单下所有下注金额总和）
            BigDecimal winAmount = getTxnsSumAmount(platformGameParent, paySerialno);

            BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
            // 会员退款
            gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            txns.setBalance(balance);
            txns.setId(null);
            txns.setStatus("Running");
            txns.setRealWinAmount(winAmount);//真实返还金额
            txns.setMethod("Cancel Bet");
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);

            oldTxns.setStatus("Adjust");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);

            // 构建返回
            JSONObject json = initSuccessResponse();
            json.put("balance", balance);
            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(-1, e.getMessage());
        }
    }

    @Override
    public Object commitReserve(BtiReserveBetsRequest reserveBetsRequest, String ip) {
        logger.info("bti_commitReserve  btiGame paramJson:{}, ip:{}", JSONObject.toJSONString(reserveBetsRequest), ip);

        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(0, "非信任來源IP");
            }

            String paySerialno = reserveBetsRequest.getReserveId();
            String userName = reserveBetsRequest.getCustId();

            // 查询订单
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(0, "用户不存在");
            }
            // reserve_id 查询是否存在
            Txns oldTxnsReserve = getTxns(gameParentPlatform, paySerialno);
            if (null == oldTxnsReserve) {
                return initFailureResponse(0, "reserve_id not found");
            }

            List<BtiBetRequest> list = reserveBetsRequest.getBetList();

            // 所有中奖下注单
            List<BtiBetRequest> winlist = list.stream().filter(o -> o.getCommomStatusID().equals(2)).collect(Collectors.toList());
            // 所有中奖注单中奖金额之和
            BigDecimal winAmount = BigDecimal.valueOf(winlist.stream().mapToDouble(o -> o.getGain().doubleValue()).sum());
            // 中奖给会员加款
            gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            BigDecimal balance = memBaseinfo.getBalance().add(winAmount);

            // 预扣订单下数据
            List<Txns> txnslist = getTxnsList(gameParentPlatform, paySerialno);
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            for (BtiBetRequest item : list) {
                for (Txns txns : txnslist) {
                    if (item.getBetID().equals(txns.getRePlatformTxId())) {
                        txns.setStatus("Settle");
                        txns.setUpdateTime(dateStr);
                        // 赢
                        if (item.getCommomStatusID().equals(2)) {
                            txns.setRealWinAmount(item.getGain());
                            txns.setWinAmount(item.getGain());
                            txns.setWinningAmount(item.getGain());
                            txns.setResultType(0);
                        } else if (item.getCommomStatusID().equals(1)) { // 输
                            // 赢:0,输:1,平手:2
                            txns.setResultType(1);
                        }
                        txnsMapper.updateById(txns);
                    }
                }
            }
            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("balance", balance);
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(0, e.getMessage());
        }
    }

    @Override
    public Object debitCustomer(BtiCreditRequest btiCreditRequest, String ip) {
        logger.info("bti_debitCustomer btiGame paramJson:{}, ip:{}", JSONObject.toJSONString(btiCreditRequest), ip);

        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(0, "非信任來源IP");
            }

            String userName = btiCreditRequest.getCustomerID();

            // 查询订单
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(0, "用户不存在");
            }

            List<BtiCreditPurchaseRequest> purchaseList = btiCreditRequest.getPurchases().getPurchaseList();
            // 所有下注金额
            BigDecimal betAmount = BigDecimal.valueOf(purchaseList.stream().mapToDouble(o -> o.getAmount().doubleValue()).sum());

            BigDecimal balance = memBaseinfo.getBalance();
            if (balance.compareTo(betAmount) < 0) {
                return initFailureResponse(0, "玩家余额不足");
            }

            GamePlatform gamePlatform = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.BTI_PLATFORM_CODE).get(0);
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());


            balance = balance.subtract(betAmount);
            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.BETNSETTLE, TradingEnum.SPENDING);

            for (BtiCreditPurchaseRequest item : purchaseList) {
                Txns oldTxns = getTxns(gameParentPlatform, item.getPurchaseID());
                // 重复订单
                if (null != oldTxns) {
                    continue;
                }

                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(item.getPurchaseID());

                //玩家 ID
                txns.setUserId(memBaseinfo.getId().toString());
                //玩家货币代码
                txns.setCurrency(gameParentPlatform.getCurrencyType());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
//            txns.setRoundId(cmdCallbackBetReq.getRound());
                txns.setGameInfo(item.getBtiSelectionsRequest().getSelectionList().stream()
                        .map(BtiCreditSelectionRequest::getLineID).collect(Collectors.joining(",")));
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
//            txns.setBetType(cmdCallbackBetReq.getGame().toString());
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
//            txns.setWinningAmount(winloseAmount);
                //玩家下注时间
                txns.setBetTime(DateUtils.format(item.getCreationDateUTC(), DateUtils.newFormat));
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
                txns.setTxTime(DateUtils.format(item.getCreationDateUTC(), DateUtils.newFormat));
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
                txnsMapper.insert(txns);
            }

            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("trx_id", purchaseList.get(0).getReserveID());
            jsonObject.put("balance", balance);
            jsonObject.put("BonusUsed", BigDecimal.ZERO);
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(0, e.getMessage());
        }
    }

    @Override
    public Object creditCustomer(BtiCreditRequest btiCreditRequest, String ip) {
        logger.info("bti_debitCustomer btiGame paramJson:{}, ip:{}", JSONObject.toJSONString(btiCreditRequest), ip);

        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(0, "非信任來源IP");
            }

            String userName = btiCreditRequest.getCustomerID();

            // 查询订单
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(0, "用户不存在");
            }

            // 所有中奖金额
            BigDecimal winAmount = btiCreditRequest.getAmount();

            GamePlatform gamePlatform = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.BTI_PLATFORM_CODE).get(0);
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());


            BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.BETNSETTLE, TradingEnum.INCOME);

            List<BtiCreditPurchaseRequest> purchaseList = btiCreditRequest.getPurchases().getPurchaseList();
            for (BtiCreditPurchaseRequest item : purchaseList) {
                Txns oldTxns = getTxns(gameParentPlatform, item.getPurchaseID());
                // 重复订单
                if (null != oldTxns) {
                    continue;
                }

                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(item.getPurchaseID());

                //玩家 ID
                txns.setUserId(memBaseinfo.getId().toString());
                //玩家货币代码
                txns.setCurrency(gameParentPlatform.getCurrencyType());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
//            txns.setRoundId(cmdCallbackBetReq.getRound());
                txns.setGameInfo(item.getBtiSelectionsRequest().getSelectionList().stream()
                        .map(BtiCreditSelectionRequest::getLineID).collect(Collectors.joining(",")));
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
                txns.setBetAmount(BigDecimal.ZERO);
                //游戏平台的下注项目
//            txns.setBetType(cmdCallbackBetReq.getGame().toString());
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(winAmount);
                //玩家下注时间
                txns.setBetTime(DateUtils.format(item.getCreationDateUTC(), DateUtils.newFormat));
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(BigDecimal.ZERO);
                //真实返还金额,游戏赢分
                txns.setRealWinAmount(winAmount);
                //此交易是否是投注 true是投注 false 否
                txns.setBet(false);
                //返还金额 (包含下注金额)
                txns.setWinAmount(winAmount);
                //有效投注金额 或 投注面值
                txns.setTurnover(BigDecimal.ZERO);
                //辨认交易时间依据
                txns.setTxTime(DateUtils.format(item.getCreationDateUTC(), DateUtils.newFormat));
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
                txnsMapper.insert(txns);
            }

            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("trx_id", purchaseList.get(0).getReserveID());
            jsonObject.put("balance", balance);
            jsonObject.put("BonusUsed", BigDecimal.ZERO);
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(0, e.getMessage());
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
     * 查询预扣款注单
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @return Txns
     */
    private List<Txns> getTxnsList(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        return txnsMapper.selectList(wrapper);
    }

    /**
     * 查询预扣款子订单是否有确认结算订单
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @return Txns
     */
    private Txns getTxns4Re(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Settle");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 查询预扣款注单下注金额
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @return Txns
     */
    private BigDecimal getTxnsSumAmount(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        List<Txns> list = txnsMapper.selectList(wrapper);
        return BigDecimal.valueOf(list.stream().mapToDouble(o -> o.getBetAmount().doubleValue()).sum());
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
//        resp.put("responseDate", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error_code", 0);
//        jsonObject.put("result", resp);
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
        jsonObject.put("error_code", error);
        jsonObject.put("error_message", description);
        return jsonObject;
    }

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.BTI_PLATFORM_CODE);
    }
}
