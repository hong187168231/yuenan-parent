package com.indo.game.service.mt.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.encrypt.MD5;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.core.mapper.game.TxnsMapper;
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
import com.indo.game.service.mt.MtService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class MtServiceImpl implements MtService {

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
    public Result mtGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("mtlog mtGame account:{},mtCodeId:{}", loginUser.getAccount(), platform);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if (0==gameParentPlatform.getIsStart()) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", MessageUtils.get("g100101",countryCode));
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
//            logger.info("站点mt余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
                cptOpenMember.setUserName(OpenAPIProperties.USER_PREFIX+loginUser.getAccount());
                cptOpenMember.setPassword(GeneratorIdUtil.generateId());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return createMemberGame(cptOpenMember, gameParentPlatform, gamePlatform, loginUser, countryCode);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);

                // 退出游戏
//                commonRequest(getLoginOutUrl(OpenAPIProperties.USER_PREFIX+loginUser.getAccount()));
                // 启动游戏
                String startUrl = getStartGame(cptOpenMember, gameParentPlatform,gamePlatform, countryCode);
                logger.info("天美log启动游戏 startUrl:{}", startUrl);
                JSONObject jsonObject = commonRequest(startUrl);
                if (null == jsonObject) {
                    return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
                }
                logger.info("天美log getStartGame启动游戏返回paramsMap:{}", jsonObject.toJSONString());
                if (jsonObject.getInteger("resultCode")==1) {
                    this.deposit(loginUser, countryCode);
                    // 请求URL
                    ApiResponseData responseData = new ApiResponseData();
                    responseData.setPathUrl(jsonObject.getString("url"));
                    logger.info("天美log getStartGame启动游戏返回成功:resultCode:{},PathUrl:{}",jsonObject.getInteger("resultCode"),jsonObject.getString("url"));
                    return Result.success(responseData);
                } else {
                    logger.info("天美log getStartGame启动游戏返回失败:resultCode:{}}",jsonObject.getInteger("resultCode"));
                    return errorCode(jsonObject.getString("resultCode"),countryCode);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.failed("g100104", MessageUtils.get("g100104",countryCode));

    }

    @Override
    public Result logout(String account,String platform, String ip,String countryCode) {
        logger.info("mtlogout mtGame account:{},code:{}", account, platform);
        try {
            Result result = allWithdraw( account,  countryCode);
            if (ResultCode.SUCCESS.getCode().equals(result.getCode())){
                // 退出
                JSONObject jsonObject = commonRequest(getLoginOutUrl(OpenAPIProperties.USER_PREFIX + account));
                logger.info("天美log logout退出游戏返回:JSONObject:{}}", jsonObject.toJSONString());
                if (null == jsonObject) {
                    return Result.failed("g091087", MessageUtils.get("g091087", countryCode));
                }
                if (jsonObject.getInteger("resultCode") == 1) {
                    logger.info("天美log logout退出游戏返回成功:resultCode:{}}", jsonObject.getInteger("resultCode"));
                    return Result.success();
                } else {
                    logger.info("天美log logout退出游戏返回失败:resultCode:{}}", jsonObject.getInteger("resultCode"));
                    return errorCode(jsonObject.getString("resultCode"), countryCode);
                }
            }else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    public Result allWithdraw(String account, String countryCode) {
        logger.info("mt_allWithdraw mtGame account:{},countryCode:{}", account, countryCode);
        try {
            String transactionId = GoldchangeEnum.DSFYXZZ.name() + GeneratorIdUtil.generateId();
            logger.info("天美log allWithdraw取出所以余额请求:apiurl:{}}", getAllWithdrawUrl(OpenAPIProperties.USER_PREFIX+account, transactionId));
            // 全部提取
            JSONObject jsonObject = commonRequest(getAllWithdrawUrl(OpenAPIProperties.USER_PREFIX+account, transactionId));
            logger.info("天美log allWithdraw取出所以余额返回:resultCode:{}}", jsonObject);
            if (null == jsonObject) {
//                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
                this.allWithdraw(account,  countryCode);
            }
            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MT_PLATFORM_CODE);
            if (jsonObject.getInteger("resultCode").equals(1)) {
                // 返回交易编码
                String transId = jsonObject.getString("transId");
                // 提出金额
                BigDecimal transCoins = null!=jsonObject.getBigDecimal("transCoins")?jsonObject.getBigDecimal("transCoins").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
                // 提出时间
                Date date = jsonObject.getDate("date");

                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);

                BigDecimal balance = memBaseinfo.getBalance().add(transCoins);
                gameCommonService.updateUserBalance(memBaseinfo, transCoins, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
                return Result.success();
            } else {
                return errorCode(jsonObject.getString("resultCode"),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }

    }

    public Result getPlayerBalance(LoginInfo loginUser, String platform, String ip,String countryCode) {
        logger.info("mt_getPlayerBalance mtGame account:{},code:{}", loginUser.getAccount(), platform);
        try {
            // 余额查询
            JSONObject jsonObject = commonRequest(getBalanceUrl(OpenAPIProperties.USER_PREFIX+loginUser.getAccount()));
            if (null == jsonObject) {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }

            if (jsonObject.getInteger("resultCode").equals(1)) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("balance", jsonObject.getBigDecimal("coinBalance"));
                return Result.success(jsonObject1);
            } else {
                return errorCode(jsonObject.getString("resultCode"),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * 充值到美天棋牌
     * @param loginUser
     * @param countryCode
     * @return
     */
    public Result deposit(LoginInfo loginUser, String countryCode) {
        logger.info("mt_deposit mtGame account:{},countryCode:{}", loginUser.getAccount(), countryCode);
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(loginUser.getAccount());
            if(BigDecimal.ZERO.compareTo(memBaseinfo.getBalance())==0){
                return Result.success();
            }
            BigDecimal balance = memBaseinfo.getBalance();
            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MT_PLATFORM_CODE);
            String transactionId = GoldchangeEnum.DSFYXZZ.name() + GeneratorIdUtil.generateId();
            logger.info("天美log deposit存入所有余额请求:resultCode:{}}", getDepositUrl(OpenAPIProperties.USER_PREFIX+loginUser.getAccount(), transactionId, balance.divide(platformGameParent.getCurrencyPro())));
            // 充值
            JSONObject jsonObject = commonRequest(getDepositUrl(OpenAPIProperties.USER_PREFIX+loginUser.getAccount(), transactionId, balance.divide(platformGameParent.getCurrencyPro())));
            logger.info("天美log deposit存入所有余额请求返回:resultCode:{}}", jsonObject);
            if (null == jsonObject) {
                this.deposit( loginUser, countryCode);
//                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }
            if (jsonObject.getInteger("resultCode").equals(1)) {
                // 返回交易编码
                String transId = jsonObject.getString("transId");
                // 提出时间
                Date date = jsonObject.getDate("date");
                // 会员平台扣款
                gameCommonService.updateUserBalance(memBaseinfo, balance, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
                balance = balance.subtract(balance);
                return Result.success();
            } else {
                return errorCode(jsonObject.getString("resultCode"),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    public Result withdraw(LoginInfo loginUser, String platform, String ip, BigDecimal coins,String countryCode) {
        logger.info("mt_withdraw mtGame account:{},code:{}", loginUser.getAccount(), platform);
        try {
            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MT_PLATFORM_CODE);
            String transactionId = GoldchangeEnum.DSFYXZZ.name() + GeneratorIdUtil.generateId();
            // 提取
            JSONObject jsonObject = commonRequest(getWithdrawUrl(OpenAPIProperties.USER_PREFIX+loginUser.getAccount(), transactionId, coins.divide(platformGameParent.getCurrencyPro())));
            if (null == jsonObject) {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }
            if (jsonObject.getInteger("resultCode").equals(1)) {
                // 返回交易编码
                String transId = jsonObject.getString("transId");
                // 提出时间
                Date date = jsonObject.getDate("date");

                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(loginUser.getAccount());
                GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
                GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());


                // 会员平台加款
                gameCommonService.updateUserBalance(memBaseinfo, coins, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
                BigDecimal balance = memBaseinfo.getBalance().add(coins);
                return Result.success();
            } else {
                return errorCode(jsonObject.getString("resultCode"),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * 登录游戏, 第一次登录会自动创建账号
     *
     * @param cptOpenMember cptOpenMember
     * @return Result
     */
    private Result createMemberGame(CptOpenMember cptOpenMember, GameParentPlatform gameParentPlatform,
                                    GamePlatform gamePlatform, LoginInfo loginUser,String countryCode) {

        // 用户注册
        JSONObject result = commonRequest(getCreateUrl(cptOpenMember, loginUser));
        if (null == result) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }
        logger.info("createMemberGame 用户注册返回JSONObject:{}", result.toJSONString());
        if (1 == result.getInteger("resultCode")||5==result.getInteger("resultCode")) {
            externalService.saveCptOpenMember(cptOpenMember);
            // 启动游戏
            String startUrl = getStartGame(cptOpenMember, gameParentPlatform,gamePlatform, countryCode);
            logger.info("createMemberGame 启动游戏startUrl:{}", startUrl);
            JSONObject jsonObject = commonRequest(startUrl);
            if (null == jsonObject) {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }
            logger.info("createMemberGame 启动游戏返回JSONObject:{}", jsonObject.toJSONString());
            if (jsonObject.getInteger("resultCode")==1) {
                this.deposit(loginUser, countryCode);
                // 请求URL
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(jsonObject.getString("url"));
                logger.info("天美log createMemberGame启动游戏返回成功:resultCode:{},PathUrl:{}",jsonObject.getInteger("resultCode"),jsonObject.getString("url"));
                return Result.success(responseData);
            } else {
                logger.info("天美log createMemberGame启动游戏返回失败:resultCode:{}",jsonObject.getInteger("resultCode"));
                return errorCode(jsonObject.getString("resultCode"),countryCode);
            }
        } else {
            return errorCode(result.getString("resultCode"),countryCode);
        }
    }

    /**
     * 公共请求
     */
    private JSONObject commonRequest(String apiUrl) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accept", "application/json");
        Map<String, Object> params = new HashMap<>();
        String returnResult = GameUtil.postJson(apiUrl, params, header);
        return JSONObject.parseObject(returnResult);
    }

    /**
     * 获取登录URL 账号
     * https://socket.zbfulipai.com/services/dg/player/playerPlatformUrl/{merchantId}/{playerName}/{pwd}/{code}/{data}
     *
     * @param cptOpenMember、 cptOpenMember
     * @return url
     */
    private String getStartGame(CptOpenMember cptOpenMember, GameParentPlatform gameParentPlatform, GamePlatform gamePlatform,String countryCode) {

        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        if(!gamePlatform.getPlatformCode().equals(gameParentPlatform.getPlatformCode())){
            jsonObject.put("gameCode", gamePlatform.getPlatformCode());
        }
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String lang = "";
        if(null!=countryCode&&!"".equals(countryCode)){
            switch (countryCode) {
                case "IN":
                    lang = "EN-US";
                    break;
                case "EN":
                    lang = "EN-US";
                    break;
                case "CN":
                    lang = "ZH-CN";
                    break;
                case "VN":
                    lang = "VI-VN";
                    break;
                case "TW":
                    lang = "ZH-TW";
                    break;
                case "TH":
                    lang = "TH-TH";
                    break;
                case "ID":
                    lang = "IN-ID";
                    break;
                case "MY":
                    lang = "MS-MY";
                    break;
                case "KR":
                    lang = "KO-KR";
                    break;
                case "JP":
                    lang = "JA-JP";
                    break;
                default:
                    lang = gameParentPlatform.getLanguageType();
                    break;
            }
        }else{
            lang = gameParentPlatform.getLanguageType();
        }
        jsonObject.put("lang", lang);
        String data = jsonObject.toJSONString();
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/playerPlatformUrl/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(cptOpenMember.getUserName()).append("/");
        url.append(MD5.md5(cptOpenMember.getPassword())).append("/");
        url.append(MD5.md5(OpenAPIProperties.MT_KEY + data)).append("/");
        url.append(getEncode(data));
        return url.toString();
    }

    /**
     * 創建用戶wm請求地址
     * /services/dg/player/playerCreate2/{playerName}/{merchantId}/{pwd}/{code}/{data}
     *
     * @return String
     */
    private String getCreateUrl(CptOpenMember cptOpenMember, LoginInfo loginUser) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/playerCreate2/");
        url.append(cptOpenMember.getUserName()).append("/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(MD5.md5(cptOpenMember.getPassword())).append("/");

        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        jsonObject.put("nickname", "");
        jsonObject.put("playerLevel", "");
        url.append(MD5.md5(OpenAPIProperties.MT_KEY + jsonObject.toJSONString())).append("/");
        url.append(getEncode(jsonObject.toJSONString()));
        return url.toString();
    }

    /**
     * 玩家退出游戏API地址
     *
     * @param userAccount userAccount
     * @return String
     */
    private String getLoginOutUrl(String userAccount) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/logOutGame/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(userAccount);
        return url.toString();
    }

    /**
     * 玩家退出游戏API地址
     *
     * @param userAccount userAccount
     * @param extTransId  转出编号
     * @return String
     */
    private String getAllWithdrawUrl(String userAccount, String extTransId) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/allWithdraw/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(userAccount).append("/").append(extTransId);
        return url.toString();
    }

    /**
     * 玩家MT余额查询
     *
     * @param userAccount userAccount
     * @return String
     */
    private String getBalanceUrl(String userAccount) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/getPlayerBalance/");
        url.append(userAccount).append("/");
        url.append(OpenAPIProperties.MT_VENDOR_ID);
        return url.toString();
    }

    /**
     * 玩家充值
     *
     * @param userAccount userAccount
     * @param extTransId  转出编号
     * @return String
     */
    private String getDepositUrl(String userAccount, String extTransId, BigDecimal coins) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/deposit2/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(userAccount).append("/");
        url.append(coins.setScale(4, RoundingMode.HALF_UP)).append("/");
        url.append(extTransId).append("/");
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        jsonObject.put("merchantId", OpenAPIProperties.MT_VENDOR_ID);
        jsonObject.put("playerName", userAccount);
        jsonObject.put("extTransId", extTransId);
        jsonObject.put("coins", coins.setScale(4, RoundingMode.HALF_UP).toString());
        url.append(MD5.md5(OpenAPIProperties.MT_KEY+jsonObject.toJSONString())).append("/");
        url.append(getEncode(jsonObject.toJSONString()));

        return url.toString();
    }

    /**
     * 玩家提现
     *
     * @param userAccount userAccount
     * @param extTransId  转出编号
     * @return String
     */
    private String getWithdrawUrl(String userAccount, String extTransId, BigDecimal coins) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/withdraw2/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(userAccount).append("/");
        url.append(coins.setScale(4, RoundingMode.HALF_UP)).append("/");
        url.append(extTransId).append("/");

        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        jsonObject.put("merchantId", OpenAPIProperties.MT_VENDOR_ID);
        jsonObject.put("playerName", userAccount);
        jsonObject.put("extTransId", extTransId);
        jsonObject.put("coins", coins.setScale(4, RoundingMode.HALF_UP).toString());

        url.append(MD5.md5(OpenAPIProperties.MT_KEY+jsonObject.toJSONString())).append("/");
        url.append(getEncode(jsonObject.toJSONString()));
        return url.toString();
    }

    /**
     * data base64code
     *
     * @param data data
     * @return String
     */
    private String getEncode(String data) {
        logger.info("getEncode.data {}", data);
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        return encoder.encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }


    public Result errorCode(String errorCode,String countryCode) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        0 创建异常
            case "0":
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
//        2 商户不存在
            case "2":
                return Result.failed("g091116", MessageUtils.get("g091116",countryCode));

//        3 商户无效
            case "3":
                return Result.failed("g091104", MessageUtils.get("g091104",countryCode));

//        5 商户用户已注册
            case "5":
                return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
            //        6 商户用户系统禁用
            case "6":
                return Result.failed("g200003", MessageUtils.get("g200003",countryCode));
            //        7 商户用户系统禁用
            case "7":
                return Result.failed("g000004", MessageUtils.get("g000004",countryCode));
// 12  商户用户游戏在线
            case "12":
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
// 15  IP被限制
            case "15":
                return Result.failed("g000003", MessageUtils.get("g000003",countryCode));
// 21 解密错误
            case "21":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            // 22 商户用户登录禁用
            case "22":
                return Result.failed("g200003", MessageUtils.get("g200003",countryCode));
// 32  可选参数错误
            case "32":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            // 33  交易编码已存在
            case "33":
                return Result.failed("g091016", MessageUtils.get("g091016",countryCode));
// 40  维护模式
            case "40":
                return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
