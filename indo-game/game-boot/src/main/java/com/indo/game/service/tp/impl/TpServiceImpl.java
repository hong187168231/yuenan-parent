package com.indo.game.service.tp.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.encrypt.MD5;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.mapper.game.GameCategoryMapper;
import com.indo.core.mapper.game.GamePlatformMapper;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.tp.TpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * jdb电子
 *
 * @author eric
 */
@Service
public class TpServiceImpl implements TpService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;
    @Autowired
    GameCategoryMapper gameCategoryMapper;
    @Autowired
    private GamePlatformMapper gamePlatformMapper;
    @Autowired
    private GameLogoutService gameLogoutService;
    /**
     * 登录游戏AWC-AE真人
     * @return loginUser 用户信息
     */
    @Override
    public Result tpGame(LoginInfo loginUser, String isMobileLogin,String ip,String platform,String parentName,String countryCode) {
        logger.info("awclog {} aeGame account:{}, aeCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if (0==gameParentPlatform.getIsStart()) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
        }
        // 是否开售校验
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
        if (null == gamePlatform) {
            return Result.failed("g100102", MessageUtils.get("g100102",countryCode));
        }
        if (0==gamePlatform.getIsStart()) {
            return Result.failed("g100102", MessageUtils.get("g100102",countryCode));
        }
        if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
            return Result.failed("g091047", MessageUtils.get("g091047",countryCode));
        }
//        //初次判断站点棋牌余额是否够该用户
//        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(loginUser.getAccount());
//        if (null==memBaseinfo){
//            return Result.failed(loginUser.getAccount()+"用户不存在");
//        }
        BigDecimal balance = loginUser.getBalance();
        //验证站点棋牌余额
        if (null==balance || BigDecimal.ZERO==balance) {
            logger.info("站点awc余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004",MessageUtils.get("g300004",countryCode));
        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
        try {
            String gamehall = "gpk2";
            // 验证且绑定（KY-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(loginUser.getAccount());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return createMemberGame(gameParentPlatform,gamePlatform, ip, cptOpenMember,isMobileLogin,gamehall,balance, countryCode);
            } else {
//                Result result = this.logout(loginUser,ip, countryCode);
//                if(null!=result&& ResultCode.SUCCESS.getCode().equals(result.getCode())) {
                    cptOpenMember.setLoginTime(new Date());
                    externalService.updateCptOpenMember(cptOpenMember);
                    //登录
                    return initGame(gameParentPlatform, gamePlatform, ip, cptOpenMember, isMobileLogin,gamehall,balance, countryCode);
//                }else {
//                    return result;
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(String account,String platform, String ip,String countryCode){
        // 验证且绑定（KY-CPT第三方会员关系）
        CptOpenMember cptOpenMember = externalService.getCptOpenMember(account, OpenAPIProperties.T9_PLATFORM_CODE);
        String gamehall = "gpk2";
        String callBackBalanceStr = getBalance(cptOpenMember,gamehall);
        if (null == callBackBalanceStr || "".equals(callBackBalanceStr) ) {
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }else {
            JSONObject jsonObjectBalance = JSONObject.parseObject(callBackBalanceStr);
            JSONObject statusJsonObjectBalance = jsonObjectBalance.getJSONObject("status");
            if (!"0".equals(statusJsonObjectBalance.getString("code"))) {
                return errorCode(statusJsonObjectBalance.getString("code"), statusJsonObjectBalance.getString("message"),countryCode);
            } else {
                JSONObject dataJsonObjectBalance = jsonObjectBalance.getJSONObject("data");
                BigDecimal amount = dataJsonObjectBalance.getBigDecimal("balance");
                if(BigDecimal.ZERO.compareTo(amount) == -1){
//                    MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
                    GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.T9_PLATFORM_CODE);
                    GamePlatform gamePlatform = new GamePlatform();
                    withdrawMoney(gameParentPlatform,gamePlatform,amount, amount, ip, cptOpenMember,"",gamehall);
                }
                Map<String, String> trr = new HashMap<>();
                trr.put("account", account);//player帳號
                trr.put("gamehall", gamehall);//遊戲廠商
                trr.put("agent_id", "");//代理id
                String signStr = "account=" + account + "&gamehall=" + "" + "&agent_id=" + OpenAPIProperties.TP_API_KEY;
                trr.put("sign", MD5.md5(signStr));//簽章
                logger.info("TP电子 logout强迫登出玩家 请求,params:{},url:{},signStr:{}", JSONObject.toJSONString(trr), "/api/player/logout", signStr);
                String callBackStr = null;
                try {
                    callBackStr = commonRequest(trr, "/api/player/logout", 0, ip, "logout");
                    logger.info("TP电子 logout强迫登出玩家 返回,params:{}", callBackStr);
                    if (null == callBackStr || "".equals(callBackStr)) {
                        return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
                    }
                    JSONObject jsonObject = JSONObject.parseObject(callBackStr);
                    JSONObject statusJsonObject = jsonObject.getJSONObject("status");
                    if ("0".equals(statusJsonObject.getString("code"))) {
                        return Result.success(statusJsonObject);
                    } else {
                        return errorCode(statusJsonObject.getString("code"), statusJsonObject.getString("message"),countryCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
                }
            }
        }
    }

    /**
     * 登录
     */
    private Result initGame(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String isMobileLogin,String gamehall,BigDecimal balance,String countryCode) throws Exception {
        String callBackStr = game(gameParentPlatform,gamePlatform, ip, cptOpenMember,isMobileLogin,gamehall,balance, countryCode);
        logger.info("TP电子 initGame登录 返回,params:{}",callBackStr);
        if (null == callBackStr || "".equals(callBackStr)) {
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }
        JSONObject jsonObject = JSONObject.parseObject(callBackStr);
        JSONObject statusJsonObject = jsonObject.getJSONObject("status");
        if("0".equals(statusJsonObject.getString("code"))){
            JSONObject dataJsonObject = jsonObject.getJSONObject("data");
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(dataJsonObject.getString("url"));
            return Result.success(responseData);
        }else {
            return errorCode(statusJsonObject.getString("code"),statusJsonObject.getString("message"),countryCode);
        }
    }
    /**
     * 创建玩家
     */
    private Result createMemberGame(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String isMobileLogin,String gamehall,BigDecimal balance,String countryCode) throws Exception {
        String callBackStr = createMember(ip, cptOpenMember);
        logger.info("TP电子 createMemberGame创建玩家 返回,params:{}",callBackStr);
        if (null == callBackStr || "".equals(callBackStr)) {
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }
        JSONObject jsonObject = JSONObject.parseObject(callBackStr);
        JSONObject statusJsonObject = jsonObject.getJSONObject("status");
        if("0".equals(statusJsonObject.getString("code"))){
            externalService.saveCptOpenMember(cptOpenMember);
            return initGame(gameParentPlatform,gamePlatform, ip, cptOpenMember,isMobileLogin,gamehall,balance, countryCode);
        }else {
            return errorCode(statusJsonObject.getString("code"),statusJsonObject.getString("message"),countryCode);
        }
    }

    /***
     * 创建玩家
     * @param ip
     * @param cptOpenMember
     * @return
     */
    public String createMember(String ip, CptOpenMember cptOpenMember) {
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("account", cptOpenMember.getUserName());//玩家帳號 a-z 0-9
            trr.put("password", cptOpenMember.getPassword());//玩家密碼 a-z 0-9
            trr.put("nickname", "");//玩家別名
            String signStr = "account="+cptOpenMember.getUserName()+"&password="+cptOpenMember.getPassword()+"&nickname="+OpenAPIProperties.TP_API_KEY;
            trr.put("sign", MD5.md5(signStr));//簽章
            logger.info("TP电子 createMember创建玩家 请求,params:{},url:{},signStr:{}",JSONObject.toJSONString(trr),"/api/player",signStr);
            return commonRequest(trr, "/api/player", cptOpenMember.getUserId(), ip, "createMember");
        } catch (Exception e) {
            logger.error("awclog game error {} ", e);
            return null;
        }
    }

    /***
     * 登录请求
     * @param gamePlatform
     * @param ip
     * @param cptOpenMember
     * @return
     */
    public String game(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String isMobileLogin,String gamehall,BigDecimal balance,String countryCode) {
        try {
            if (deposit(gameParentPlatform,gamePlatform, balance, balance.subtract(balance), ip, cptOpenMember, "",gamehall)) {
//            Map<String, String> trr = new HashMap<>();
//            trr.put("gamehall","");//	string (query string)	Required	遊戲廠商
//            trr.put("gamecode","");//	string (query string)	Required	遊戲代碼
//            trr.put("account","");//	string (query string)	Required	帳戶名稱
//            trr.put("lang","");//	string (query string)	Required	語言代碼 (en, zh-cn, zh-tw, th, ja, vi, id)
//            trr.put("platform","");//	string (query string)	Optional	平台類型 (web, mobile)
//            trr.put("sub_gamehall","");//	string (query string)	Optional	子遊戲商
//            trr.put("return_url","");//	string (query string)	Optional	遊戲退出連結(預設為關閉視窗)
//            trr.put("is_free_trial","");//	number (query string)	Optional	是否為試玩(0 => false, 1 => true, 預設為0)
//            trr.put("register_url","");//	string (query string)	Optional	串接平台的註冊連結
//            trr.put("room_id","");//	string (query string)	Optional	遊戲房間id
//            trr.put("agent_id","");//	string (query string)	Optional	代理id
//            trr.put("bounsmode","");//	string (query string)	Optional	獎金組
//            trr.put("sign","");//	string (query string)	Required	簽章
                //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
                String lang = "";
                if(null!=countryCode&&!"".equals(countryCode)){
                    switch (countryCode) {
                        case "IN":
                            lang = "en";
                            break;
                        case "EN":
                            lang = "en";
                            break;
                        case "CN":
                            lang = "zh-CN";
                            break;
                        case "VN":
                            lang = "vi";
                            break;
                        case "TW":
                            lang = "zh-TW";
                            break;
                        case "TH":
                            lang = "th";
                            break;
                        case "ID":
                            lang = "id";
                            break;
                        case "JP":
                            lang = "ja";
                            break;
                        default:
                            lang = gameParentPlatform.getLanguageType();
                            break;
                    }
                }else{
                    lang = gameParentPlatform.getLanguageType();
                }
                String signStr = "gamehall=" + gamehall + "&gamecode=" + gamePlatform.getPlatformCode() + "&account=" + cptOpenMember.getUserName() + "&lang=" + lang + "&platform=" + isMobileLogin + "&sub_gamehall=" + "&return_url=" + "&is_free_trial=" + "&register_url=" + "&room_id=" + "&agent_id=" + "&bounsmode=" + OpenAPIProperties.TP_API_KEY;
                String params = "gamehall=" + gamehall + "&gamecode=" + gamePlatform.getPlatformCode() + "&account=" + cptOpenMember.getUserName() + "&lang=" + lang + "&platform=" + isMobileLogin + "&sub_gamehall=" + "&return_url=" + "&is_free_trial=" + "&register_url=" + "&room_id=" + "&agent_id=" + "&bounsmode=" + "&sign=" + MD5.md5(signStr);
                logger.info("TP电子 createMember创建玩家 请求,params:{},url:{},signStr:{}", params, "/api/game/game-link", signStr);
                return commonRequestGet(params, "/api/game/game-link");
            }
            return null;
        } catch (Exception e) {
            logger.error("awclog game error {} ", e);
            return null;
        }
    }

    /***
     * 查询余额
     * @param cptOpenMember
     * @return
     */
    public String getBalance(CptOpenMember cptOpenMember,String gamehall) {
        try {
            String signStr = "account="+cptOpenMember.getUserName()+"&agent_id="+""+"&gamehall="+gamehall+OpenAPIProperties.TP_API_KEY;
            String params = "account="+cptOpenMember.getUserName()+"&agent_id="+""+"&gamehall="+"&sign="+MD5.md5(signStr);
            logger.info("TP电子 getBalance查询余额 请求,params:{},url:{},signStr:{}",params,"/api/game/game-link",signStr);
            return commonRequestGet(params, "/api/player/wallet");
        } catch (Exception e) {
            logger.error("awclog game error {} ", e);
            return null;
        }
    }

    /***
     * 取款
     * @param ip
     * @param cptOpenMember
     * @return
     */
    public Boolean withdrawMoney(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform,BigDecimal amount, BigDecimal balance,  String ip, CptOpenMember cptOpenMember,String transaction_id,String gamehall) {
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("gamehall", gamehall);//	string (formdata)	Required	遊戲廠商
            trr.put("account", cptOpenMember.getUserName());//	string (formdata)	Required	玩家帳號
            if(null == transaction_id || "".equals(transaction_id)){
                transaction_id = GeneratorIdUtil.generateId();
            }
            trr.put("transaction_id", transaction_id);//	string (formdata)	Required	交易代碼，格式請參考API注意事項
            trr.put("amount", String.valueOf(amount));//	number (formdata)	Required	金額(格式需有小數點後兩位 EX:500.00)
            String signStr = "account="+cptOpenMember.getUserName()+"&amount="+amount+"&gamehall="+gamehall+"&transaction_id="+transaction_id+OpenAPIProperties.TP_API_KEY;
            trr.put("sign", MD5.md5(signStr));//簽章
            logger.info("TP电子 withdrawMoney取款 请求,params:{},url:{},signStr:{}",JSONObject.toJSONString(trr),"/api/player",signStr);
            String callBackStr = commonRequest(trr, "/api/transaction/withdraw", cptOpenMember.getUserId(), ip, "createMember");
            logger.info("TP电子 withdrawMoney取款 返回,params:{}",callBackStr);
            if (null == callBackStr || "".equals(callBackStr)) {
                return this.withdrawMoney(gameParentPlatform,gamePlatform, amount,balance,ip,cptOpenMember,transaction_id,gamehall);
            }else {
                JSONObject jsonObject = JSONObject.parseObject(callBackStr);
                JSONObject statusJsonObject = jsonObject.getJSONObject("status");
                if ("0".equals(statusJsonObject.getString("code"))) {
                    this.addGameTxns(gameParentPlatform,gamePlatform, transaction_id, cptOpenMember,balance, amount, ip, false);
                    return true;
                } else {
                    return this.withdrawMoney(gameParentPlatform,gamePlatform, amount,balance,ip, cptOpenMember, transaction_id,gamehall);
                }
            }
        } catch (Exception e) {
            logger.error("awclog game error {} ", e);
            return this.withdrawMoney(gameParentPlatform,gamePlatform, amount,balance,ip,cptOpenMember,transaction_id,gamehall);
        }
    }

    /***
     * 存款
     * @param ip
     * @param cptOpenMember
     * @return
     */
    public Boolean deposit(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform,BigDecimal amount, BigDecimal balance, String ip, CptOpenMember cptOpenMember,String transaction_id,String gamehall) {
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("gamehall", gamehall);//	string (formdata)	Required	遊戲廠商
            trr.put("account", cptOpenMember.getUserName());//	string (formdata)	Required	玩家帳號
            if(null == transaction_id || "".equals(transaction_id)){
                transaction_id = GeneratorIdUtil.generateId();
            }
            trr.put("transaction_id", transaction_id);//	string (formdata)	Required	交易代碼，格式請參考API注意事項
            trr.put("amount", String.valueOf(amount.setScale(2)));//	number (formdata)	Required	金額(格式需有小數點後兩位 EX:500.00)
            String signStr = "account="+cptOpenMember.getUserName()+"&amount="+amount.setScale(2)+"&gamehall="+""+"&transaction_id="+transaction_id+OpenAPIProperties.TP_API_KEY;
            trr.put("sign", MD5.md5(signStr));//簽章
            logger.info("TP电子 deposit存款 请求,params:{},url:{},signStr:{}",JSONObject.toJSONString(trr),"/api/transaction/deposit",signStr);
            String callBackStr = commonRequest(trr, "/api/transaction/deposit", cptOpenMember.getUserId(), ip, "createMember");
            logger.info("TP电子 deposit存款 返回,params:{}",callBackStr);
            if (null == callBackStr || "".equals(callBackStr)) {
                return this.deposit(gameParentPlatform,gamePlatform, amount,balance,ip,cptOpenMember,transaction_id, gamehall);
            }else {
                JSONObject jsonObject = JSONObject.parseObject(callBackStr);
                JSONObject statusJsonObject = jsonObject.getJSONObject("status");
                if ("0".equals(statusJsonObject.getString("code"))) {
                    this.addGameTxns(gameParentPlatform,gamePlatform, transaction_id, cptOpenMember,balance, amount, ip, true);
                    return true;
                } else {
                    return this.deposit(gameParentPlatform,gamePlatform, amount,balance, ip, cptOpenMember, transaction_id, gamehall);
                }
            }
        } catch (Exception e) {
            logger.error("awclog game error {} ", e);
            return this.deposit(gameParentPlatform,gamePlatform, amount,balance,ip,cptOpenMember,transaction_id, gamehall);
        }
    }

    public void addGameTxns(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String transaction_id, CptOpenMember cptOpenMember,BigDecimal balance,BigDecimal amount,String ip,boolean b){
        GameCategory gameCategory;
        if(null!=gamePlatform){
            gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        }else {
            gameCategory = gameCommonService.getGameCategoryById(Long.valueOf(2));
        }
        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(transaction_id);
        //此交易是否是投注 true是投注 false 否
//        txns.setbet();
        //玩家 ID
        txns.setUserId(cptOpenMember.getUserName());
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
        if(null!=gamePlatform) {
            //平台游戏代码
            txns.setGameCode(gamePlatform.getPlatformCode());
            //游戏名称
            txns.setGameName(gamePlatform.getPlatformEnName());
        }
        //游戏平台的下注项目
//        txns.setbetType();
        //下注金额
        txns.setBetAmount(amount);
        //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
        if (b){//转入 取款
            txns.setWinningAmount(amount);
        }else {//转出 存款
            txns.setWinningAmount(amount.negate());
        }

        //玩家下注时间
//        txns.setBetTime(DateUtils.formatByString(apiRequestData.getGameDate(), DateUtils.newFormat));
        //游戏商的回合识别码
//        txns.setRoundId(null!=apiRequestData.getGameSeqNo()?apiRequestData.getGameSeqNo().toString():"");
        //游戏讯息会由游戏商以 JSON 格式提供
//        txns.setgameInfo();
        //更新时间 (遵循 ISO8601 格式)
//        txns.setUpdateTime(DateUtils.formatByString(apiRequestData.getLastModifyTime(), DateUtils.newFormat));
        //真实下注金额,需增加在玩家的金额
//        txns.setRealBetAmount(apiRequestData.getBet());
        //真实返还金额,游戏赢分
//        txns.setRealWinAmount(apiRequestData.getWin());
        //返还金额 (包含下注金额)
//        txns.setWinAmount();
        //赌注的结果 : 赢:0,输:1,平手:2
//        int resultTyep;
//        if (apiRequestData.getNetWin().compareTo(BigDecimal.ZERO)==0){
//            resultTyep = 2;
//        }else if(apiRequestData.getNetWin().compareTo(BigDecimal.ZERO)==1){
//            resultTyep = 0;
//        }else {
//            resultTyep = 1;
//        }
//        txns.setResultType(resultTyep);
        //有效投注金额 或 投注面值
//        txns.setTurnover(apiRequestData.getDenom());
        //辨认交易时间依据
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        txns.setCreateTime(dateStr);
        txns.setTxTime(dateStr);
        //返回单号
//        txns.setrePlatformTxId();
        //返还金额当局的游戏商注单号
//        txns.setrefundPlatformTxId();
        //请依据此参数来决定结算方法
//        txns.setsettleType();
        //- 2 Void game 游戏无效、现场操作问题等
//- 9 Cheat (hide in the report) 有作弊 (不会呈现在后台报表)
        //标示无效的原因   jdb免费游戏0: 否 1: 是
//        txns.setVoidType(null!=apiRequestData.getHasFreegame()?apiRequestData.getHasFreegame().toString():"");
        //判断玩家当前余额是否足够负担此笔 betNSettle 请求所需的金额 jdb彩金贡献值
//        txns.setRequireAmount(apiRequestData.getJackpotContribute());
        //玩家获得的活动派彩 jdb赢得彩金金额
//        txns.setAmount(apiRequestData.getJackpotWin());
        //活动的交易代码
//        txns.setpromotionTxId();
        //活动代码
//        txns.setpromotionId();
        //活动种类的代码
//        txns.setpromotionTypeId();
        //打赏给直播主的金额 *jdb玩家合理最小余额
//        txns.setTip(apiRequestData.getMb());
        //赔率
//        txns.setodds();
        //赔率类型
//        txns.setoddsType();
        //打赏资讯，此参数仅游戏商有提供资讯时才会出现
//        txns.settipinfo();
        //操作状态
        txns.setStatus("Running");
        //操作名称
        if (b){//转入 取款
            txns.setMethod("Settle");
        }else {//转出 存款
            txns.setMethod("Place Bet");
        }

        //余额
        txns.setBalance(balance);
        //投注 IP
        txns.setBetIp(ip);//  string 是 投注 IP
        //代理编号
//        txns.setagentId();//  long 是 代理编号
        //组别佣金代码
//        txns.setgroupComm();//  string 是 组别佣金代码
        //混合过关类型 ID
//        txns.setmpId();//  int 是 混合过关类型 ID
        //投注方式{1:PC,2:Wap,4:Smart}
//        if("Web".equals(apiRequestData.getClientType())){
//
//        }
//        txns.setbetWay();//  int 是 投注方式
        //注单排序值
//        txns.setSortNo(apiRequestData.getSessionNo());
        //报表日期
//        txns.setReportDate(DateUtils.formatByString(apiRequestData.getReportDate(), DateUtils.newFormat));
        //奖金游戏
//        txns.setHasBonusGame(apiRequestData.getHasBonusGame());// Integer 奖金游戏0: 否   1: 是
        //博取游戏
//        txns.setHasGamble(apiRequestData.getHasGamble());// Integer 博取游戏0: 否  1: 是
        //游戏区域
//        txns.setRoomType(apiRequestData.getRoomType());
        // Integer 游戏区域
        //-1:大厅（成就游戏）
        // 0:小压码区
        //1:中压码区
        //2:大压码区
        //※各压码区称号会依据机台类型有所不同
        txnsMapper.insert(txns);
    }
    /**
     * 公共请求
     */
    public String commonRequest(Map<String, String> paramsMap, String url, Integer userId, String ip, String type) throws Exception {
        return GameUtil.doProxyPostJsonMultipart(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,OpenAPIProperties.TP_API_URL+url, paramsMap, type, userId,OpenAPIProperties.TP_API_TOKEN);
    }

    public String commonRequestGet(String params, String url) throws Exception {
        return GameUtil.sendGet(OpenAPIProperties.TP_API_URL+url, params);
    }

    public Result  errorCode(String errorCode,String errorMessage,String countryCode) {
        return Result.failed("g009999",MessageUtils.get("g009999",countryCode));
    }
}
