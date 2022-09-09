package com.indo.game.service.pp.impl;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.PPHashAESEncrypt;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.dto.pp.PpApiGetBalanceReq;
import com.indo.game.pojo.dto.pp.PpApiRequestData;
import com.indo.game.pojo.dto.pp.PpApiResponseData;
import com.indo.game.pojo.dto.pp.PpApiStartGameReq;
import com.indo.game.pojo.dto.pp.PpApiTransferReq;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.pp.PpService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PpServiceImpl implements PpService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;
    @Autowired
    private GameLogoutService gameLogoutService;
    @Override
    public Result ppGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("pplog ppGame account:{},ppCodeId:{}", loginUser.getId(), loginUser.getAccount(), platform);
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

//        BigDecimal balance = loginUser.getBalance();
//        //验证站点余额
//        if (null == balance || balance.compareTo(BigDecimal.ZERO) == 0) {
//            logger.info("站点pp余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
        try {

            // 验证且绑定（KY-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(GeneratorIdUtil.generateId());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                createMemberGame(cptOpenMember, countryCode);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);

                // 退出游戏
//                loginOutPP(loginUser);
            }
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
                        lang = "zh";
                        break;
                    case "VN":
                        lang = "vi";
                        break;
                    case "TW":
                        lang = "zh";
                        break;
                    case "TH":
                        lang = "th";
                        break;
                    case "ID":
                        lang = "in";
                        break;
                    case "MY":
                        lang = "ms";
                        break;
                    case "KR":
                        lang = "ko";
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
            PpApiStartGameReq ppApiRequestData = new PpApiStartGameReq();
            ppApiRequestData.setSecureLogin(OpenAPIProperties.PP_SECURE_LOGIN);
            ppApiRequestData.setExternalPlayerId(loginUser.getAccount());
            ppApiRequestData.setGameId(platform);
            ppApiRequestData.setLanguage(lang);
            return startGame(ppApiRequestData, ip, countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    @Override
    public Result logout(String account,String platform, String ip,String countryCode) {
        logger.info("pplogout ppGame account:{},platform:{}", account, platform);
        try {
            // 退出游戏
            PpApiResponseData ppCommonResp = loginOutPP(account);

            if (0 == ppCommonResp.getError()) {
                return Result.success(ppCommonResp);
            } else {
                return errorCode(ppCommonResp.getError().toString(), ppCommonResp.getDescription(),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    private PpApiResponseData loginOutPP(String account) throws Exception {
        PpApiRequestData ppApiRequestData = new PpApiRequestData();
        ppApiRequestData.setSecureLogin(OpenAPIProperties.PP_SECURE_LOGIN);
        ppApiRequestData.setExternalPlayerId(account);

        // 获取请求参数
        Map<String, Object> params = getPostParams(ppApiRequestData);

        // 退出游戏
        return commonRequest(getLogOutPpPlayerUrl(), params, 0, "loginoutPP");
    }

    @Override
    public Result transfer(PpApiTransferReq ppApiTransferReq, String ip,String countryCode) {
        logger.info("pp_transfer ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppApiTransferReq), ip);
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppApiTransferReq.getExternalPlayerId());
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.PP_PLATFORM_CODE).get(0);
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.PP_PLATFORM_CODE);

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 存提金额
            BigDecimal betAmount = ppApiTransferReq.getAmount();
            if (memBaseinfo.getBalance().compareTo(betAmount) < 0) {
                return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
            }
            // 存提转账交易ID
            String transactionId = GoldchangeEnum.DSFYXZZ.name() + GeneratorIdUtil.generateId();
            ppApiTransferReq.setExternalTransactionId(transactionId);

            // 交互第三方
            PpApiRequestData ppApiRequestData = new PpApiRequestData();
            ppApiRequestData.setSecureLogin(OpenAPIProperties.PP_SECURE_LOGIN);
            ppApiRequestData.setExternalPlayerId(memBaseinfo.getAccount());
            ppApiRequestData.setExternalTransactionId(transactionId);
            ppApiRequestData.setAmount(betAmount);

            // 获取请求参数
            Map<String, Object> params = getPostParams(ppApiRequestData);

            // 转入PP
            PpApiResponseData ppApiResponseData = commonRequest(
                    getTransferUrl(), params,
                    memBaseinfo.getId(), "transferPP");

            if (0 != ppApiResponseData.getError()) {
                return errorCode(ppApiResponseData.getError().toString(), ppApiResponseData.getDescription(),countryCode);
            }
            // 大于0， 从平台转出， 转入PP电子
            if (betAmount.compareTo(BigDecimal.ZERO) > 0) {

                balance = balance.subtract(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
            }
            // 小于0， 从PP电子转出， 转入平台
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {

                balance = balance.add(betAmount.abs());
                gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
            }

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(transactionId);
            //此交易是否是投注 true是投注 false 否
            txns.setBet(false);
            //玩家 ID
            txns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
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
            txns.setBetAmount(ppApiTransferReq.getAmount());
            // PP交易ID
            txns.setRePlatformTxId(ppApiResponseData.getTransactionId());
            //游戏平台的下注项目
//            txns.setBetType(ppApiTransferReq.getGameId());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
//            txns.setWinningAmount(BigDecimal.ZERO);
            //玩家下注时间
            txns.setBetTime(DateUtils.format(new Date(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(ppApiTransferReq.getAmount());
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(BigDecimal.ZERO);
            //有效投注金额 或 投注面值
            txns.setTurnover(ppApiTransferReq.getAmount());
            //辨认交易时间依据
            txns.setTxTime(DateUtils.format(new Date(), DateUtils.newFormat));
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
                int count = 0;
                // 失败重试
                while (count < 5) {
                    num = txnsMapper.insert(txns);
                    if (num > 0) break;
                    count++;
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }

        return Result.success();
    }

    @Override
    public Result getBalance(PpApiGetBalanceReq ppApiGetBalanceReq, String ip,String countryCode) {
        logger.info("pp_getBalance ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppApiGetBalanceReq), ip);
        PpApiResponseData ppApiResponseData;
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppApiGetBalanceReq.getExternalPlayerId());

            if (null == memBaseinfo) {
                return Result.failed("g010001", MessageUtils.get("g010001",countryCode));
            }

            // 交互第三方
            PpApiRequestData ppApiRequestData = new PpApiRequestData();
            ppApiRequestData.setSecureLogin(OpenAPIProperties.PP_SECURE_LOGIN);
            ppApiRequestData.setExternalPlayerId(memBaseinfo.getAccount());

            Map<String, Object> params = getPostParams(ppApiRequestData);

            // 查询余额
            ppApiResponseData = commonRequest(
                    getBalanceUrl(), params,
                    memBaseinfo.getId(), "getBalancePP");

            if (0 != ppApiResponseData.getError()) {
                return errorCode(ppApiResponseData.getError().toString(), ppApiResponseData.getDescription(),countryCode);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }

        return Result.success(JSONObject.toJSONString(ppApiResponseData));
    }

    @Override
    public Result startGame(PpApiStartGameReq ppApiStartGameReq, String ip,String countryCode) {
        logger.info("pp_startGame ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppApiStartGameReq), ip);
        PpApiResponseData ppApiResponseData;
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppApiStartGameReq.getExternalPlayerId());

            if (null == memBaseinfo) {
                return Result.failed("g010001", MessageUtils.get("g010001",countryCode));
            }

            // 交互第三方
            ppApiStartGameReq.setSecureLogin(OpenAPIProperties.PP_SECURE_LOGIN);
            Map<String, Object> params = getPostParams(ppApiStartGameReq);

            // 查询API
            ppApiResponseData = commonRequest(
                    getStartGameUrl(), params,
                    memBaseinfo.getId(), "startGamePP");

            if (0 != ppApiResponseData.getError()) {
                return errorCode(ppApiResponseData.getError().toString(), ppApiResponseData.getDescription(),countryCode);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }

        ApiResponseData responseData = new ApiResponseData();
        responseData.setPathUrl(ppApiResponseData.getGameURL());
        return Result.success(responseData);
    }

    /**
     * 转换请求对象为map, 加密参数
     *
     * @param obj
     * @return
     */
    private Map<String, Object> getPostParams(Object obj) {
        Map<String, Object> params = objectToMap(obj);
        params.put("hash", PPHashAESEncrypt.encrypt(obj, OpenAPIProperties.PP_API_SECRET_KEY));
        return params;
    }

    /**
     * 对象转MAP
     *
     * @param obj
     * @return
     */
    private Map<String, Object> objectToMap(Object obj) {
        String json = JSONObject.toJSONString(obj);
        return JSONObject.parseObject(json);
    }

    /**
     * 创建游戏账号
     *
     * @param cptOpenMember
     * @return
     */
    private Result createMemberGame(CptOpenMember cptOpenMember,String countryCode) {
        // 创建PP账号
        PpApiResponseData ppApiResponseData = createPpMember(cptOpenMember);
        if (null == ppApiResponseData) {
            return Result.failed("g091087", "第三方请求异常！");
        }

        if (0 == ppApiResponseData.getError()) {
            cptOpenMember.setUserName(ppApiResponseData.getPlayerId());
            externalService.saveCptOpenMember(cptOpenMember);
            return Result.success();
        } else {
            return errorCode(ppApiResponseData.getError().toString(), ppApiResponseData.getDescription(), countryCode);
        }
    }

    /**
     * 创建PP账号
     *
     * @param cptOpenMember、
     * @return
     */
    private PpApiResponseData createPpMember(CptOpenMember cptOpenMember) {
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.PP_PLATFORM_CODE);
        PpApiRequestData ppApiRequestData = new PpApiRequestData();
        ppApiRequestData.setSecureLogin(OpenAPIProperties.PP_SECURE_LOGIN);
        ppApiRequestData.setExternalPlayerId(cptOpenMember.getUserName());
        ppApiRequestData.setCurrency(platformGameParent.getCurrencyType());

        Map<String, Object> params = getPostParams(ppApiRequestData);

        PpApiResponseData ppApiResponseData = null;
        try {
            ppApiResponseData = commonRequest(
                    getCreatePpPlayerUrl(), params,
                    cptOpenMember.getUserId(), "createPpPlayer");
        } catch (Exception e) {
            logger.error("pplog createPpMember:{}", e);
            e.printStackTrace();
        }
        return ppApiResponseData;

    }

    /**
     * 公共请求
     */
    public PpApiResponseData commonRequest(String apiUrl, Map<String, Object> params, Object userId, String method) throws Exception {
        logger.info("pplog commonRequest userId:{},paramsMap:{}", userId, params);
        String result = GameUtil.postForm4PP(apiUrl, params, method);
        PpApiResponseData response = JSONObject.parseObject(result, PpApiResponseData.class);
        return response;
    }

    /**
     * 创建玩家API地址
     *
     * @return
     */
    private String getCreatePpPlayerUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.PP_API_URL).append("/player/account/create/");
        return builder.toString();
    }

    /**
     * 玩家退出游戏API地址
     *
     * @return
     */
    private String getLogOutPpPlayerUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.PP_API_URL).append("/game/session/terminate/");
        return builder.toString();
    }

    /**
     * 获取余额
     *
     * @return
     */
    private String getBalanceUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.PP_API_URL).append("/balance/current/");
        return builder.toString();
    }

    /**
     * 存提
     *
     * @return
     */
    private String getTransferUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.PP_API_URL).append("/balance/transfer");
        return builder.toString();
    }

    /**
     * 启动游戏
     *
     * @return
     */
    private String getStartGameUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.PP_API_URL).append("/game/start/");
        return builder.toString();
    }


    public Result errorCode(String errorCode, String errorMessage,String countryCode) {
//        200 成功。                                                Succeed.
        switch (errorCode) {
//        1 内部错误。请重试
            case "1":
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
//        100 {错误描述}。请稍后重试。” （GetTransferStatus 方法）           No authorized to access
            case "100":
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));

//        2 安全登录名和安全密码组合错误                            Domain is null or the length of domain less than 2.
            case "2":
                return Result.failed("g100107", MessageUtils.get("g100107",countryCode));

//        6 未找到游戏或系统不允许游戏                                        Failed to pass the domain validation.
            case "6":
                return Result.failed("g100102", MessageUtils.get("g100102",countryCode));

//        7一个或多个输入参数未设置或设置错误。   The encrypted data is null or the length of the encrypted data is equal to 0.
            case "7":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));

//        8 交易已存在            Assertion(SAML) didn't pass the timestamp validation.
            case "8":
                return Result.failed("g091016", MessageUtils.get("g091016",countryCode));

//        17 未找到玩家。                      Failed to extract the SAML parameters from the encrypted data.
            case "17":
                return Result.failed("g010001", MessageUtils.get("g010001",countryCode));

//        21 货币代码错误或不受支持。                                            Unknow action.
            case "21":
                return Result.failed("g100001", MessageUtils.get("g100001",countryCode));
//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
