package com.indo.game.service.tcg.impl;

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
import com.indo.game.pojo.vo.callback.tcgwin.*;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.tcg.TCGWinCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TCGWinCallbackServiceImpl implements TCGWinCallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object getUserBalance(TCGWinGetBalanceCallBackReq tcgWinGetBalanceCallBackReq, String ip) {
        TCGWinGetBalanceCallBackResponse tcgWinGetBalanceCallBackResponse = new TCGWinGetBalanceCallBackResponse();
        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                tcgWinGetBalanceCallBackResponse.setStatus(4);
                tcgWinGetBalanceCallBackResponse.setError_desc("Merchant is not allowed for this product type");
                return tcgWinGetBalanceCallBackResponse;
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(tcgWinGetBalanceCallBackReq.getUsername());
            if (null == memBaseinfo) {
                tcgWinGetBalanceCallBackResponse.setStatus(15);
                tcgWinGetBalanceCallBackResponse.setError_desc("User Does Not Exists");
                return tcgWinGetBalanceCallBackResponse;
            }
            tcgWinGetBalanceCallBackResponse.setStatus(0);
            tcgWinGetBalanceCallBackResponse.setBalance(memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
            return tcgWinGetBalanceCallBackResponse;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            tcgWinGetBalanceCallBackResponse.setStatus(1);
            tcgWinGetBalanceCallBackResponse.setError_desc("Unknown system error, please contact TCG customer support");
            return tcgWinGetBalanceCallBackResponse;
        }
    }

    // Credit API 增加玩家帐户金额接口
    @Override
    public Object debit(TCGWinDebitCallBackReq<TransactionData> tcgWinDebitCallBackReq, String ip) {
        TCGWinDebitCallBackResponse tcgWinDebitCallBackResponse = new TCGWinDebitCallBackResponse();
        try {

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                tcgWinDebitCallBackResponse.setStatus(4);
                tcgWinDebitCallBackResponse.setError_desc("Merchant is not allowed for this product type");
                return tcgWinDebitCallBackResponse;
            }
            List userList = new ArrayList();
            for (TransactionData transactionData:tcgWinDebitCallBackReq.getTransactions()){
                if (null == transactionData.getUsername()|| "".equals(transactionData.getUsername())) {
                    tcgWinDebitCallBackResponse.setStatus(22);
                    tcgWinDebitCallBackResponse.setError_desc("Parameter Validation Failed");
                    return tcgWinDebitCallBackResponse;
                }

                // 查询玩家是否存在
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(transactionData.getUsername());
                if (null == memBaseinfo) {
                    tcgWinDebitCallBackResponse.setStatus(15);
                    tcgWinDebitCallBackResponse.setError_desc("User Does Not Exists");
                    return tcgWinDebitCallBackResponse;
                }
    //            3250	Debit	投注/扣錢
    //            3200	Third Party Betting	第三方游戏投注
    //            4200	Bet	彩票游戏投注
    //            4203	Bet Confirm	投注确认
    //            4206	Chase Bet	追号单扣费
    //            4220	ELott Betting	Elott 彩票投注
    //            4223	Elott Confirm Betting	Elott 彩票投注確認
    //            4225	Elott Chase Bet	Elott 追號單扣費
    //            4240	SEA Betting	SEA东南亚彩票投注
                // 会员余额
                BigDecimal balance = memBaseinfo.getBalance();
                // 下注金额
                BigDecimal betAmount = null != transactionData.getAmount() ? transactionData.getAmount().multiply(platformGameParent.getCurrencyPro()) : BigDecimal.ZERO;
                String txnid = transactionData.getRef_no();
                if (balance.compareTo(betAmount) < 0) {
                    tcgWinDebitCallBackResponse.setStatus(16);
                    tcgWinDebitCallBackResponse.setError_desc("Insufficient merchant credit to fund in");
                    return tcgWinDebitCallBackResponse;
                }
                // 查询用户请求订单
                Txns oldTxns = getTxns(platformGameParent, txnid);
                if (null != oldTxns ) {
                    tcgWinDebitCallBackResponse.setStatus(0);
                    tcgWinDebitCallBackResponse.setError_desc("Success");
                    return tcgWinDebitCallBackResponse;
                }
                balance = balance.subtract(betAmount);

                // 更新玩家余额
                gameCommonService.updateUserBalance(memBaseinfo, betAmount.subtract(betAmount),
                        GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

                GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(tcgWinDebitCallBackReq.getProduct_type().toString(), platformGameParent.getPlatformCode());
                GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(txnid);

                //玩家 ID
                txns.setUserId(memBaseinfo.getAccount());
                //玩家货币代码
                txns.setCurrency(platformGameParent.getCurrencyType());
                //平台代码
                txns.setPlatform(platformGameParent.getPlatformCode());
                //平台英文名称
                txns.setPlatformEnName(platformGameParent.getPlatformEnName());
                //平台中文名称
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
                //平台游戏类型
                txns.setGameType(gameCategory.getGameType());
                //下注金额
                txns.setBetAmount(betAmount);
                txns.setWinAmount(betAmount);
                txns.setWinningAmount(betAmount.negate());
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
    //            txns.setWinningAmount(freeWinAmount);
                //玩家下注时间
                txns.setBetTime(DateUtils.formatByLong(tcgWinDebitCallBackReq.getRequest_time(), DateUtils.newFormat));
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(betAmount);
                //真实返还金额,游戏赢分
    //            txns.setRealWinAmount(freeWinAmount);
                //返还金额 (包含下注金额)
    //            txns.setWinAmount(freeWinAmount);
                //有效投注金额 或 投注面值
                txns.setTurnover(betAmount);
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
                BalanceInfo balanceInfo = new BalanceInfo();
                balanceInfo.setBalance(balance.divide(platformGameParent.getCurrencyPro()));
                balanceInfo.setUsername(transactionData.getUsername());
                userList.add(balanceInfo);
            int num = txnsMapper.insert(txns);
        }
            tcgWinDebitCallBackResponse.setStatus(0);
            tcgWinDebitCallBackResponse.setError_desc("Success");
            tcgWinDebitCallBackResponse.setBalance_info(userList);
            return tcgWinDebitCallBackResponse;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            tcgWinDebitCallBackResponse.setStatus(1);
            tcgWinDebitCallBackResponse.setError_desc("Unknown system error, please contact TCG customer support");
            return tcgWinDebitCallBackResponse;
        }
    }
    // Debit API扣除玩家帐户金额接口
    @Override
    public Object credit (TCGWinDebitCallBackReq<TransactionData> tcgWinDebitCallBackReq, String ip) {
        TCGWinDebitCallBackResponse tcgWinDebitCallBackResponse = new TCGWinDebitCallBackResponse();
        try {

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                tcgWinDebitCallBackResponse.setStatus(4);
                tcgWinDebitCallBackResponse.setError_desc("Merchant is not allowed for this product type");
                return tcgWinDebitCallBackResponse;
            }
            List userList = new ArrayList();
            for (TransactionData transactionData:tcgWinDebitCallBackReq.getTransactions()){
                if (null == transactionData.getUsername()|| "".equals(transactionData.getUsername())) {
                    tcgWinDebitCallBackResponse.setStatus(22);
                    tcgWinDebitCallBackResponse.setError_desc("Parameter Validation Failed");
                    return tcgWinDebitCallBackResponse;
                }

                // 查询玩家是否存在
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(transactionData.getUsername());
                if (null == memBaseinfo) {
                    tcgWinDebitCallBackResponse.setStatus(15);
                    tcgWinDebitCallBackResponse.setError_desc("User Does Not Exists");
                    return tcgWinDebitCallBackResponse;
                }
                // 会员余额
                BigDecimal balance = memBaseinfo.getBalance();
                // 下注金额
                BigDecimal amount = null != transactionData.getAmount() ? transactionData.getAmount().multiply(platformGameParent.getCurrencyPro()) : BigDecimal.ZERO;
                String txnid = transactionData.getRef_no();
                if (balance.compareTo(amount) < 0) {
                    tcgWinDebitCallBackResponse.setStatus(16);
                    tcgWinDebitCallBackResponse.setError_desc("Insufficient merchant credit to fund in");
                    return tcgWinDebitCallBackResponse;
                }
                // 查询用户请求订单
                Txns oldTxns = getTxns(platformGameParent, txnid);
                if (null != oldTxns ) {
                    tcgWinDebitCallBackResponse.setStatus(0);
                    tcgWinDebitCallBackResponse.setError_desc("Success");
                    return tcgWinDebitCallBackResponse;
                }
                String method = "Settle";
                if("3151".equals(transactionData.getTx_type())||"4108".equals(transactionData.getTx_type())
                        ||"4109".equals(transactionData.getTx_type())||"4111".equals(transactionData.getTx_type())
                        ||"4121".equals(transactionData.getTx_type())||"4123".equals(transactionData.getTx_type())
                        ||"4124".equals(transactionData.getTx_type())||"4125".equals(transactionData.getTx_type())
                        ||"4126".equals(transactionData.getTx_type())||"4141".equals(transactionData.getTx_type())
                        ||"4143".equals(transactionData.getTx_type())||"4144".equals(transactionData.getTx_type())
                        ||"4145".equals(transactionData.getTx_type())||"4146".equals(transactionData.getTx_type())
                        ||"4147".equals(transactionData.getTx_type())||"4149".equals(transactionData.getTx_type())
                        ||"4151".equals(transactionData.getTx_type())||"4153".equals(transactionData.getTx_type())
                        ||"4154".equals(transactionData.getTx_type())||"4155".equals(transactionData.getTx_type())
                        ||"4156".equals(transactionData.getTx_type())||"4157".equals(transactionData.getTx_type())
                        ||"4161".equals(transactionData.getTx_type())||"4163".equals(transactionData.getTx_type())
                        ||"4164".equals(transactionData.getTx_type())||"4165".equals(transactionData.getTx_type())
                        ||"4166".equals(transactionData.getTx_type())||"4167".equals(transactionData.getTx_type())){
//                3151	Cancel Bet	取消下注
//                4108	Empty Cancel Bet	空开撤单
//                4109	System Cancel Bet	系統撤单
//                4111	User Cancel Bet	个人撤单
//                4121	Elott User Cancel Order	Elott 个人撤单
//                4123	Elott Hit Draw Back	Elott 追中撤单
//                4124	Elott Shown Draw Back	Elott 出號撤單
//                4125	Elott Draw cancelled	Elott 空開撤單
//                4126	Elott System Draw Back	Elott 系統撤單
//                4141	SEA User Draw Back	SEA东南亚彩票个人撤单
//                4143	SEA Hit Draw Back	SEA东南亚彩票追中撤单
//                4144	SEA Win Amount Cancel	SEA东南亚彩票出號撤單
//                4145	SEA Draw cancelled	SEA东南亚彩票空開撤單
//                4146	SEA System Draw Back	SEA东南亚彩票系統撤單
//                4147	SEA Super Draw Back	SEA东南亚彩票超級撤單
//                4149	SEA Betting Failed Refund	SEA东南亚彩票投注失敗退款
//                4151	VN User Draw Back	VN越南彩个人撤单
//                4153	VN Hit Draw Back	VN越南彩追中撤单
//                4154	VN Win Amt Cancel	VN越南彩出號撤單
//                4155	VN Draw cancelled	VN越南彩空開撤單
//                4156	VN System Draw Back	VN越南彩系統撤單
//                4157	VN Super Draw Back	VN越南彩超級撤單
//                4161	VN Elott User Draw Back	VN越南彩盤口个人撤单
//                4163	VN Elott Hit Draw Back	VN越南彩盤口追中撤单
//                4164	VN Elott Win Amt Cancel	VN越南彩盤口出號撤單
//                4165	VN Elott Draw cancelled	VN越南彩盤口空開撤單
//                4166	VN Elott System Draw Back	VN越南彩盤口系統撤單
//                4167	VN Elott Super Draw Back	VN越南彩盤口超級撤單
                    balance = balance.add(amount.subtract(oldTxns.getWinAmount()));
                    gameCommonService.updateUserBalance(memBaseinfo, amount.subtract(oldTxns.getWinAmount()), GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                    method = "Cancel Bet";
                }else {

                    balance = balance.add(amount.subtract(oldTxns.getWinAmount()));
                    gameCommonService.updateUserBalance(memBaseinfo, amount.subtract(oldTxns.getWinAmount()), GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                }
                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setRoundId(txnid);
                txns.setMethod(method);
                txns.setStatus("Running");
                txns.setWinningAmount(amount);
                txns.setWinAmount(amount);
                txnsMapper.insert(txns);
                oldTxns.setStatus(method);
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                oldTxns.setUpdateTime(dateStr);
                BalanceInfo balanceInfo = new BalanceInfo();
                balanceInfo.setBalance(balance.divide(platformGameParent.getCurrencyPro()));
                balanceInfo.setUsername(transactionData.getUsername());
                userList.add(balanceInfo);
                int num = txnsMapper.insert(txns);
            }
            tcgWinDebitCallBackResponse.setStatus(0);
            tcgWinDebitCallBackResponse.setError_desc("Success");
            tcgWinDebitCallBackResponse.setBalance_info(userList);
            return tcgWinDebitCallBackResponse;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            tcgWinDebitCallBackResponse.setStatus(1);
            tcgWinDebitCallBackResponse.setError_desc("Unknown system error, please contact TCG customer support");
            return tcgWinDebitCallBackResponse;
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
                .or().eq(Txns::getMethod, "Settle"));
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

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SGWIN_PLATFORM_CODE);
    }
}
