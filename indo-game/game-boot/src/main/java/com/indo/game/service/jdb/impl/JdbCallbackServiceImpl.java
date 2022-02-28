package com.indo.game.service.jdb.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.jdb.JdbApiBetRequestData;
import com.indo.game.pojo.dto.jdb.JdbApiCancelBetRequestData;
import com.indo.game.pojo.dto.jdb.JdbApiGetBalanceRequestData;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.vo.callback.jdb.JdbApiResponseData;
import com.indo.game.pojo.vo.callback.jdb.JdbApiResponseError;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.jdb.JdbCallbackService;
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
public class JdbCallbackServiceImpl implements JdbCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    public Object jdbCallback(String jsonStr, String ip) {
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        Integer action = Integer.valueOf(jsonObject.getString("action"));
        if (!checkIp(ip)) {
            JdbApiResponseError callBackFail = new JdbApiResponseError();
            callBackFail.setStatus("9001");
            callBackFail.setErr_text("No authorized to access.");
            return callBackFail;
        }
        try {//Get Balance 取得玩家余额
            if (6==action) {
                return getBalance(jsonStr);
            }
            //Place Bet 下注信息及游戏结果
            if (8==action) {
                return bet(jsonStr);
            }
            //Cancel Bet 取消下注
            if (4==action) {
                return cancelBet(jsonStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("awcCallBack {9999Fail} callBack 回调,IP:" + ip + " params:{}", e);
            JdbApiResponseError callBackFail = new JdbApiResponseError();
            callBackFail.setStatus("9999");
            callBackFail.setErr_text("Failed.");
            return callBackFail;
        }
        return "";
    }

    //Get Balance 取得玩家余额
    private Object getBalance(String jsonStr) {
        JdbApiGetBalanceRequestData apiRequestData = JSON.parseObject(jsonStr,new TypeReference<JdbApiGetBalanceRequestData>() {
        });
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(apiRequestData.getUid());
        if (null == memBaseinfo) {
            JdbApiResponseError callBackFail = new JdbApiResponseError();
            callBackFail.setStatus("7501");
            callBackFail.setErr_text("User ID cannot be found.");
            return callBackFail;
        } else {
            JdbApiResponseData getBalanceSuccess = new JdbApiResponseData();
            getBalanceSuccess.setStatus("0000");
            getBalanceSuccess.setBalance(memBaseinfo.getBalance());

            return getBalanceSuccess;
        }
    }


    //Place Bet 下注
    private Object bet(String jsonStr) {
        JdbApiBetRequestData apiRequestData = JSON.parseObject(jsonStr, new TypeReference<JdbApiBetRequestData>() {
        });

        BigDecimal balance = BigDecimal.ZERO;
        String userId = apiRequestData.getUid();
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(apiRequestData.getMType().toString());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(gamePlatform.getParentName());
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
        balance = memBaseinfo.getBalance();
        if (null == memBaseinfo) {
            JdbApiResponseError callBackFail = new JdbApiResponseError();
            callBackFail.setStatus("7501");
            callBackFail.setErr_text("User ID cannot be found.");
            return callBackFail;
        }
        BigDecimal betAmount = apiRequestData.getNetWin();

        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, apiRequestData.getTransferId());
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        wrapper.eq(Txns::getUserId, apiRequestData.getUid());
        Txns oldTxns = txnsMapper.selectOne(wrapper);

        if(apiRequestData.getNetWin().compareTo(BigDecimal.ZERO)==1){//赢
            balance = balance.add(betAmount);
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
        }
        if(apiRequestData.getNetWin().compareTo(BigDecimal.ZERO)==-1){//输

            if (memBaseinfo.getBalance().compareTo(betAmount.abs()) == -1) {
                JdbApiResponseError callBackFail = new JdbApiResponseError();
                callBackFail.setStatus("6002");
                callBackFail.setErr_text("User balance is zero");
                return callBackFail;
            }
            balance = balance.subtract(betAmount.abs());
            gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
        }
        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(apiRequestData.getTransferId().toString());
        //此交易是否是投注 true是投注 false 否
//        txns.setbet();
        //玩家 ID
        txns.setUserId(apiRequestData.getUid());
        //玩家货币代码
        txns.setCurrency(apiRequestData.getCurrency());
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
        //游戏平台的下注项目
//        txns.setbetType();
        //下注金额
        txns.setBetAmount(apiRequestData.getBet());
        //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
        txns.setWinningAmount(apiRequestData.getNetWin());
        //玩家下注时间
        txns.setBetTime(DateUtils.formatByString(apiRequestData.getGameDate(), DateUtils.newFormat));
        //游戏商的回合识别码
        txns.setRoundId(null!=apiRequestData.getGameSeqNo()?apiRequestData.getGameSeqNo().toString():"");
        //游戏讯息会由游戏商以 JSON 格式提供
//        txns.setgameInfo();
        //更新时间 (遵循 ISO8601 格式)
        txns.setUpdateTime(DateUtils.formatByString(apiRequestData.getLastModifyTime(), DateUtils.newFormat));
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(apiRequestData.getBet());
        //真实返还金额,游戏赢分
        txns.setRealWinAmount(apiRequestData.getWin());
        //返还金额 (包含下注金额)
//        txns.setWinAmount();
        //赌注的结果 : 赢:0,输:1,平手:2
        int resultTyep;
        if (apiRequestData.getNetWin().compareTo(BigDecimal.ZERO)==0){
            resultTyep = 2;
        }else if(apiRequestData.getNetWin().compareTo(BigDecimal.ZERO)==1){
            resultTyep = 0;
        }else {
            resultTyep = 1;
        }
        txns.setResultType(resultTyep);
        //有效投注金额 或 投注面值
        txns.setTurnover(apiRequestData.getDenom());
        //辨认交易时间依据
        txns.setTxTime(null!=apiRequestData.getTs()?apiRequestData.getTs().toString():"");
        //返回单号
//        txns.setrePlatformTxId();
        //返还金额当局的游戏商注单号
//        txns.setrefundPlatformTxId();
        //请依据此参数来决定结算方法
//        txns.setsettleType();
        //- 2 Void game 游戏无效、现场操作问题等
//- 9 Cheat (hide in the report) 有作弊 (不会呈现在后台报表)
        //标示无效的原因   jdb免费游戏0: 否 1: 是
        txns.setVoidType(null!=apiRequestData.getHasFreegame()?apiRequestData.getHasFreegame().toString():"");
        //判断玩家当前余额是否足够负担此笔 betNSettle 请求所需的金额 jdb彩金贡献值
        txns.setRequireAmount(apiRequestData.getJackpotContribute());
        //玩家获得的活动派彩 jdb赢得彩金金额
        txns.setAmount(apiRequestData.getJackpotWin());
        //活动的交易代码
//        txns.setpromotionTxId();
        //活动代码
//        txns.setpromotionId();
        //活动种类的代码
//        txns.setpromotionTypeId();
        //打赏给直播主的金额 *jdb玩家合理最小余额
        txns.setTip(apiRequestData.getMb());
        //赔率
//        txns.setodds();
        //赔率类型
//        txns.setoddsType();
        //打赏资讯，此参数仅游戏商有提供资讯时才会出现
//        txns.settipinfo();
        //操作状态
        txns.setStatus("Running");
        //操作名称
        if (0==apiRequestData.getSystemTakeWin()){
            txns.setMethod("Place Bet");
        }else {
            txns.setMethod("Settle");
        }

        //余额
        txns.setBalance(balance);
        //创建时间
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        txns.setCreateTime(dateStr);
        //投注 IP
        txns.setBetIp(apiRequestData.getIpAddress());//  string 是 投注 IP
        //代理编号
//        txns.setagentId();//  long 是 代理编号
        //组别佣金代码
//        txns.setgroupComm();//  string 是 组别佣金代码
        //混合过关类型 ID
//        txns.setmpId();//  int 是 混合过关类型 ID
        //投注方式{1:PC,2:Wap,4:Smart}
        if("Web".equals(apiRequestData.getClientType())){

        }
//        txns.setbetWay();//  int 是 投注方式
        //注单排序值
        txns.setSortNo(apiRequestData.getSessionNo());
        //报表日期
        txns.setReportDate(DateUtils.formatByString(apiRequestData.getReportDate(), DateUtils.newFormat));
        //奖金游戏
        txns.setHasBonusGame(apiRequestData.getHasBonusGame());// Integer 奖金游戏0: 否   1: 是
        //博取游戏
        txns.setHasGamble(apiRequestData.getHasGamble());// Integer 博取游戏0: 否  1: 是
        //游戏区域
        txns.setRoomType(apiRequestData.getRoomType());
        // Integer 游戏区域
        //-1:大厅（成就游戏）
        // 0:小压码区
        //1:中压码区
        //2:大压码区
        //※各压码区称号会依据机台类型有所不同
        if(oldTxns!=null) {
            txns.setStatus("Settle");
            txnsMapper.updateById(oldTxns);
        }
        txnsMapper.insert(txns);
        JdbApiResponseData jdbApiResponseData = new JdbApiResponseData();
        jdbApiResponseData.setStatus("0000");
        jdbApiResponseData.setBalance(balance);
        return jdbApiResponseData;
    }

//        gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.AWCAESEXYBCRT_IN.getValue(), balance, memBaseinfo, content, cptOpenMember, Constants.AWC_AESEXYBCRT_ACCOUNT_TYPE);

    //Cancel Bet 取消注单
    private Object cancelBet(String jsonStr) {
        JdbApiCancelBetRequestData apiRequestData = JSON.parseObject(jsonStr,new TypeReference<JdbApiCancelBetRequestData>() {
        });


        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(apiRequestData.getUid());
        if (null == memBaseinfo) {
            JdbApiResponseError callBackFail = new JdbApiResponseError();
            callBackFail.setStatus("7501");
            callBackFail.setErr_text("User ID cannot be found.");
            return callBackFail;
        }
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, apiRequestData.getTransferId());
        wrapper.eq(Txns::getUserId, apiRequestData.getUid());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if(!"Cancel Bet".equals(oldTxns.getMethod())) {

            //查询下注订单
            BigDecimal betAmount = oldTxns.getBetAmount();
            if(oldTxns.getWinningAmount().compareTo(BigDecimal.ZERO)==1){//赢
                balance = balance.subtract(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
            }
            if(oldTxns.getWinningAmount().compareTo(BigDecimal.ZERO)==-1){//输
                balance = balance.add(betAmount.abs());
                gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
            }


            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            txns.setId(null);
            txns.setBalance(balance);
            txns.setMethod("Cancel Bet");
            txns.setStatus("Running");
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);
            oldTxns.setStatus("Cancel");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);
        }
        JdbApiResponseData jdbApiResponseData = new JdbApiResponseData();
        jdbApiResponseData.setStatus("0000");
        jdbApiResponseData.setBalance(balance);
        return jdbApiResponseData;
    }


    private boolean checkIp(String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode("JDB");
        if (null == gameParentPlatform) {
            return false;
        } else if (null == gameParentPlatform.getIpAddr() || "".equals(gameParentPlatform.getIpAddr())) {
            return true;
        } else if (gameParentPlatform.getIpAddr().equals(ip)) {
            return true;
        }
        return false;
    }

}
