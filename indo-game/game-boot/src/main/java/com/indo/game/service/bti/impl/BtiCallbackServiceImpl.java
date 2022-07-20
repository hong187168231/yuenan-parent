package com.indo.game.service.bti.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.bti.*;
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

            StringBuilder builder = new StringBuilder();
            builder.append("error_code=0\n");
            builder.append("error_message=No error\n");
            builder.append("cust_id=").append(memBaseinfo.getAccount()).append("\n");
            builder.append("balance=").append(memBaseinfo.getBalance()).append("\n");
            builder.append("cust_login=").append(memBaseinfo.getAccount()).append("\n");
            builder.append("city=").append(gameParentPlatform.getCity()).append("\n");
            builder.append("country=").append(gameParentPlatform.getCountry()).append("\n");
            builder.append("currency_code=").append(gameParentPlatform.getCurrencyType()).append("\n");

            return builder.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(-1, e.getMessage());
        }
    }

    @Override
    public Object reserve(BtiReserveRequst btiReserveRequst, String ip) {

        try {
            String paySerialno = btiReserveRequst.getReserve_id();
            String userName = btiReserveRequst.getCust_id();
            BigDecimal betAmount = btiReserveRequst.getAmount();

            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(-2, "用户不存在",BigDecimal.ZERO,paySerialno);
            }
            BigDecimal balance = memBaseinfo.getBalance();
            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(-6, "非信任來源IP",balance,paySerialno);
            }

            if (balance.compareTo(betAmount) < 0) {
                return initFailureResponse(-4, "玩家余额不足",balance,paySerialno);
            }
            // 查询订单
            Txns oldTxns = getReserveTxns(gameParentPlatform, paySerialno);
            // 重复订单
            if (null != oldTxns) {
                StringBuilder builder = new StringBuilder();
                builder.append("error_code=0\n");
                builder.append("error_message=No error\n");
                builder.append("trx_id=").append(paySerialno).append("\n");
                builder.append("balance=").append(balance);
                return builder.toString();
            }
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
            if (null == gamePlatform) {
                return initFailureResponse(-4, "游戏不存在",balance,paySerialno);
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            if(betAmount.compareTo(BigDecimal.ZERO)!=0) {
                balance = balance.subtract(betAmount);
                // 更新余额
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
            }

//            List<BtiBetRequest> list = reserveBetsRequest.getBetList();
//            String betTypeId = null;
//            BigDecimal odds = BigDecimal.ZERO;
//
//            for (BtiBetRequest item : list) {
//                odds = BigDecimal.valueOf(item.getOdds());
//                betTypeId = item.getBetTypeID();
//            }

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(paySerialno);

            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(gameParentPlatform.getCurrencyType());
//            txns.setOdds(odds);

            // lineid拼接写入游戏信息中
//            txns.setGameInfo(list.stream().map(a -> a.getBetID()).collect(Collectors.joining(",")));
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
//            txns.setBetType(betTypeId);
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount.negate());
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            //玩家下注时间
            txns.setBetTime(dateStr);
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
            txns.setTxTime(dateStr);
            //操作名称
            txns.setMethod("Reserve");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);
            //创建时间

            txns.setCreateTime(dateStr);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                int count = 0;
                // 失败重试
                while (count < 5) {
                    num = txnsMapper.insert(txns);
                    if (num > 0) break;
                    count++;
                }
            }

            StringBuilder builder = new StringBuilder();
            builder.append("error_code=0\n");
            builder.append("error_message=No error\n");
            builder.append("trx_id=").append(paySerialno).append("\n");
            builder.append("balance=").append(balance);
            return builder.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(-1, e.getMessage());
        }
    }

    @Override
    public Object debitReserve(BtiDebitReserveRequst btiDebitReserveRequst, String ip) {

        try {
            String paySerialno = btiDebitReserveRequst.getReserve_id();
            String userName = btiDebitReserveRequst.getCust_id();
            String reqId = btiDebitReserveRequst.getReq_id();

            // 查询订单
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(0, "用户不存在",BigDecimal.ZERO,paySerialno);
            }
            BigDecimal balance = memBaseinfo.getBalance();
            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(0, "非信任來源IP",balance,paySerialno);
            }

            // reserve_id 查询是否存在
            Txns oldTxnsReserve = getReserveTxns(gameParentPlatform, paySerialno);
            if (null == oldTxnsReserve) {
                return initFailureResponse(0, "reserve_id not found",balance,paySerialno);
            }

            // req_id 查询是否存在
            Txns oldReqTxnsReserve = getDebitReserveTxns(gameParentPlatform, paySerialno, reqId);
            if (null != oldReqTxnsReserve) {
                return initFailureResponse(0, "req_id is already debit",balance,paySerialno);
            }

            // // 写入新的debit订单，用于后续commit进行下注金额比对
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxnsReserve, txns);
            txns.setRePlatformTxId(reqId);
            txns.setBalance(balance);
            txns.setId(null);
            txns.setTurnover(btiDebitReserveRequst.getAmount());
            txns.setBetAmount(btiDebitReserveRequst.getAmount());
            txns.setWinningAmount(btiDebitReserveRequst.getAmount().negate());
            txns.setRealBetAmount(btiDebitReserveRequst.getAmount());
            txns.setMethod("Place Bet");
            //操作名称
            txns.setStatus("Reserve");
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);
//            oldTxnsReserve.setUpdateTime(dateStr);
//            oldTxnsReserve.setStatus("Reserve");
//            txnsMapper.updateById(oldTxnsReserve);

            StringBuilder builder = new StringBuilder();
            builder.append("error_code=0\n");
            builder.append("error_message=No error\n");
            builder.append("trx_id=").append(reqId).append("\n");
            builder.append("balance=").append(balance);
            return builder.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(0, e.getMessage());
        }
    }

    @Override
    public Object cancelReserve(BtiCancelReserveRequst btiCancelReserveRequst, String ip) {

        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(-6, "非信任來源IP");
            }

            String paySerialno = btiCancelReserveRequst.getReserve_id();
            String userName = btiCancelReserveRequst.getCust_id();
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            BigDecimal balance = memBaseinfo.getBalance();
            if (null == memBaseinfo) {
                return initFailureResponse(-2, "用户不存在");
            }
// reserve_id 查询是否存在
            Txns oldTxnsCancelReserveTxns = getCancelReserveTxns(platformGameParent, paySerialno);
            if (null != oldTxnsCancelReserveTxns) {
                StringBuilder builder = new StringBuilder();
                builder.append("error_code=0\n");
                builder.append("error_message=").append("Reserve was not found").append("\n");
                builder.append("balance=").append(balance);
                return builder.toString();
            }
            // 查询用户请求订单
            Txns oldTxns = getReserveTxns(platformGameParent, paySerialno);
            if (null == oldTxns) {
                StringBuilder builder = new StringBuilder();
                builder.append("error_code=0\n");
                builder.append("error_message=").append("Reserve was not found").append("\n");
                builder.append("balance=").append(memBaseinfo.getBalance());
                return builder.toString();
            }

            // 回退金额（预扣款注单下注金额）
            BigDecimal betAmount = oldTxns.getAmount();
            if(betAmount.compareTo(BigDecimal.ZERO)!=0) {
                balance = memBaseinfo.getBalance().add(betAmount);
                // 会员退款
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            }
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            txns.setBalance(balance);
            txns.setId(null);
            txns.setStatus("Running");
            txns.setRealWinAmount(betAmount);//真实返还金额
            txns.setMethod("Cancel Bet");
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);
            // 更新预扣注单状态
            oldTxns.setUpdateTime(dateStr);
            oldTxns.setStatus("Cancel Bet");
            txnsMapper.updateById(oldTxns);

            // 构建返回
            StringBuilder builder = new StringBuilder();
            builder.append("error_code=0\n");
            builder.append("error_message=No error\n");
            builder.append("balance=").append(balance);
            return builder.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(-1, e.getMessage());
        }
    }

    @Override
    public Object commitReserve(BtiCommitReserveRequst btiCommitReserveRequst, String ip) {

        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(0, "非信任來源IP");
            }

            String paySerialno = btiCommitReserveRequst.getReserve_id();
            String userName = btiCommitReserveRequst.getCust_id();

            // 查询订单
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(0, "用户不存在");
            }
            BigDecimal balance = memBaseinfo.getBalance();
            Txns oldTxnsCommitReserve = commitReserve(gameParentPlatform, paySerialno);
            if (null == oldTxnsCommitReserve) {
                StringBuilder builder = new StringBuilder();
                builder.append("error_code=0\n");
                builder.append("error_message=No error\n");
                builder.append("trx_id=").append(paySerialno).append("\n");
                builder.append("balance=").append(balance);
                return builder.toString();
            }
            // reserve_id 查询是否存在
            Txns oldTxnsReserve = getReserveTxns(gameParentPlatform, paySerialno);
            if (null == oldTxnsReserve) {
                return initFailureResponse(0, "reserve_id not found");
            }

            // 预扣款金额
            BigDecimal betAmount = oldTxnsReserve.getBetAmount();

            // 预扣订单下数据
            List<Txns> txnslist = getTxnsHasDebit(gameParentPlatform, paySerialno);
            // 提交debit注单金额总和
            BigDecimal debitAmount = BigDecimal.valueOf(txnslist.stream().mapToDouble(o -> o.getBetAmount().doubleValue()).sum());

            // 预扣款金额比下注金额多， 需要回退用户金额
            if (betAmount.compareTo(debitAmount) == 1) {
                // 回退部分预扣金额
                gameCommonService.updateUserBalance(memBaseinfo, betAmount.subtract(debitAmount), GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                balance = balance.add(betAmount.subtract(debitAmount));

            }
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            for(Txns oldTxns :txnslist){
                // 更新预扣注单状态
                oldTxns.setUpdateTime(dateStr);
                oldTxns.setStatus("commitReserve");
                txnsMapper.updateById(oldTxnsReserve);
            }
            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxnsReserve, txns);
            txns.setBetAmount(debitAmount);
            txns.setWinAmount(debitAmount);
            txns.setWinningAmount(debitAmount.negate());
            txns.setCreateTime(dateStr);
            txns.setMethod("Place Bet");
            //操作名称
            txns.setStatus("Running");
            txnsMapper.insert(txns);

            StringBuilder builder = new StringBuilder();
            builder.append("error_code=0\n");
            builder.append("error_message=No error\n");
            builder.append("trx_id=").append(paySerialno).append("\n");
            builder.append("balance=").append(balance);
            return builder.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(0, e.getMessage());
        }
    }

    //重新结算
    @Override
    public Object debitCustomer(BtiDebitCustomerRequst btiDebitCustomerRequst, String ip) {

        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(0, "非信任來源IP");
            }

            String userName = btiDebitCustomerRequst.getCust_id();
            String reqId = btiDebitCustomerRequst.getReq_id();
            // 查询订单
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(0, "用户不存在");
            }

            // 扣款
            BigDecimal betAmount = btiDebitCustomerRequst.getAmount();
            BigDecimal balance = memBaseinfo.getBalance();

            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            if(betAmount.compareTo(BigDecimal.ZERO)!=0) {
                balance = balance.subtract(betAmount);
                // 更新余额
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
            }


            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(reqId);

            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(gameParentPlatform.getCurrencyType());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
//            txns.setRoundId(cmdCallbackBetReq.getRound());
//            txns.setGameInfo(item.getBtiSelectionsRequest().getSelectionList().stream()
//                    .map(BtiCreditSelectionRequest::getLineID).collect(Collectors.joining(",")));
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
            txns.setWinAmount(betAmount.negate());
            txns.setWinningAmount(betAmount.negate());
            //游戏平台的下注项目
//            txns.setBetType(cmdCallbackBetReq.getGame().toString());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
//            txns.setWinningAmount(winloseAmount);
            //创建时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            //玩家下注时间
            txns.setBetTime(dateStr);
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(betAmount);
            //真实返还金额,游戏赢分
//            txns.setRealWinAmount(winloseAmount);
            //此交易是否是投注 true是投注 false 否
            txns.setBet(false);
            //返还金额 (包含下注金额)
//            txns.setWinAmount(winloseAmount);
            //有效投注金额 或 投注面值
            txns.setTurnover(betAmount);
            //辨认交易时间依据
            txns.setTxTime(dateStr);
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);
            txns.setCreateTime(dateStr);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP
            txnsMapper.insert(txns);

            StringBuilder builder = new StringBuilder();
            builder.append("error_code=0\n");
            builder.append("error_message=No error\n");
            builder.append("trx_id=").append(reqId).append("\n");
            builder.append("balance=").append(balance);
            return builder.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(0, e.getMessage());
        }
    }

    //结算
    @Override
    public Object creditCustomer(BtiCreditCustomerRequst btiCreditCustomerRequst, String ip) {

        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(0, "非信任來源IP");
            }

            String userName = btiCreditCustomerRequst.getCust_id();
            String reqId = btiCreditCustomerRequst.getReq_id();

            // 查询订单
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(0, "用户不存在");
            }
            BigDecimal balance = memBaseinfo.getBalance();

            Txns oldTxns = creditCustomerReserve(gameParentPlatform, reqId);
            // 重复订单
            if (null != oldTxns) {
                StringBuilder builder = new StringBuilder();
                builder.append("error_code=0\n");
                builder.append("error_message=No error\n");
                builder.append("trx_id=").append(reqId).append("\n");
                builder.append("balance=").append(balance);
                return builder.toString();
            }

            // 所有中奖金额
            BigDecimal winAmount = btiCreditCustomerRequst.getAmount();

            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            if(winAmount.compareTo(BigDecimal.ZERO)!=0) {
                balance = memBaseinfo.getBalance().add(winAmount);
                // 更新余额
                gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(reqId);

            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(gameParentPlatform.getCurrencyType());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
//            txns.setRoundId(cmdCallbackBetReq.getRound());
//            txns.setGameInfo(item.getBtiSelectionsRequest().getSelectionList().stream()
//                    .map(BtiCreditSelectionRequest::getLineID).collect(Collectors.joining(",")));
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
            //创建时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            txns.setWinningAmount(winAmount);
            //玩家下注时间
            txns.setBetTime(dateStr);
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
            txns.setTxTime(dateStr);
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);
            txns.setCreateTime(dateStr);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP
            txnsMapper.insert(txns);

            StringBuilder builder = new StringBuilder();
            builder.append("error_code=0\n");
            builder.append("error_message=No error\n");
            builder.append("trx_id=").append(reqId).append("\n");
            builder.append("balance=").append(balance);
            return builder.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(0, e.getMessage());
        }
    }


    /**
     * 查询预扣款注单, 不查询debit注单
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @return Txns
     */
    private Txns getReserveTxns(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getMethod, "Reserve");
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 查询预扣款注单, 不查询debit注单
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @return Txns
     */
    private Txns getCancelReserveTxns(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getMethod, "Cancel Bet");
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 查询已经提交注单
     * @param gameParentPlatform
     * @param paySerialno
     * @return
     */
    private Txns commitReserve(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        return txnsMapper.selectOne(wrapper);
    }

    private Txns getDebitReserveTxns(GameParentPlatform gameParentPlatform, String paySerialno, String rePlatformTxId) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Txns::getMethod, "Place Bet");
        wrapper.and(c -> c.eq(Txns::getStatus, "Reserve").or().eq(Txns::getStatus, "commitReserve"));
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        if (StringUtils.isNotEmpty(rePlatformTxId)) {
            wrapper.eq(Txns::getRePlatformTxId, rePlatformTxId);
        }
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 查询预扣款注单下的debit 注单
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @return Txns
     */
    private List<Txns> getTxnsHasDebit(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getMethod, "Place Bet");
        wrapper.eq(Txns::getStatus, "Reserve");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
//        wrapper.isNotNull(Txns::getRePlatformTxId);
        return txnsMapper.selectList(wrapper);
    }

    /**
     * 结算
     * @param gameParentPlatform
     * @param paySerialno
     * @return
     */
    private Txns creditCustomerReserve(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Settle"));
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
     * 初始化交互失败返回
     *
     * @param error       错误码
     * @param description 错误描述
     * @return JSONObject
     */
    private String initFailureResponse(Integer error, String description) {
        StringBuilder builder = new StringBuilder();
        builder.append("error_code=").append(error).append("\n");
        builder.append("error_message=").append(description).append("\n");
        return builder.toString();
    }

    private String initFailureResponse(Integer error, String description,BigDecimal balance,String trx_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("error_code=").append(error).append("\n");
        builder.append("error_message=").append(description).append("\n");
        builder.append("trx_id=").append(trx_id).append("\n");
        builder.append("balance=").append(balance).append("\n");
        return builder.toString();
    }

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.BTI_PLATFORM_CODE);
    }
}
