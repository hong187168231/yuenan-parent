package com.indo.game.service.rich.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.StringUtils;
import com.indo.game.common.util.RichSHAEncrypt;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.rich.Rich88ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.rich.Rich88Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class Rich88ServiceImpl implements Rich88Service {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;

    @Override
    public Result rich88Game(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("rich88log {} rich88Game account:{},Rich88CodeId:{}", loginUser.getId(), loginUser.getAccount(), platform);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("(" + parentName + ")游戏平台不存在");
        }
        if ("0".equals(gameParentPlatform.getIsStart())) {
            return Result.failed("g" + "100101", "游戏平台未启用");
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", gameParentPlatform.getMaintenanceContent());
        }
        if (!platform.equals(parentName)) {
            GamePlatform gamePlatform;
            // 是否开售校验
            gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
            if (null == gamePlatform) {
                return Result.failed("(" + platform + ")平台游戏不存在");
            }
            if ("0".equals(gamePlatform.getIsStart())) {
                return Result.failed("g" + "100102", "游戏未启用");
            }
            if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
                return Result.failed("g091047", gamePlatform.getMaintenanceContent());
            }
        }

        BigDecimal balance = loginUser.getBalance();
        //验证站点余额
        if (null == balance || BigDecimal.ZERO == balance) {
            logger.info("站点rich88余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004", "会员余额不足");
        }

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

            } else {
                CptOpenMember updateCptOpenMember = new CptOpenMember();
                updateCptOpenMember.setId(cptOpenMember.getId());
                updateCptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(updateCptOpenMember);
            }

            // 第一次登录自动创建玩家, 后续登录返回登录游戏URL
            return createMemberGame(cptOpenMember, platform, loginUser.getLanguage());

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    @Override
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        logger.info("rich88logout {} rich88Game account:{},rich88CodeId:{}", loginUser.getId(), loginUser.getAccount(), platform);
        try {
            // 退出游戏
            Rich88ApiResponseData rich88ApiResponseData =
                    commonRequest(getLoginOutUrl(loginUser.getAccount()), null, loginUser.getId());

            if (0 == rich88ApiResponseData.getCode()) {
                return Result.success(rich88ApiResponseData);
            } else {
                return errorCode(rich88ApiResponseData.getCode().toString(), rich88ApiResponseData.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }

    }

    /**
     * 登录游戏, 第一次登录会自动创建账号
     *
     * @param cptOpenMember cptOpenMember
     * @return Result
     */
    private Result createMemberGame(CptOpenMember cptOpenMember, String platform, String lang) {
        // 创建rich88账号
        Rich88ApiResponseData rich88ApiResponseData = createRich88Member(cptOpenMember, platform, lang);
        if (null == rich88ApiResponseData) {
            return Result.failed("g091087", "第三方请求异常！");
        }

        if (0 == rich88ApiResponseData.getCode()) {
            if (null == cptOpenMember.getId()) {
                externalService.saveCptOpenMember(cptOpenMember);
            }
            ApiResponseData responseData = new ApiResponseData();
            JSONObject urlJson = JSONObject.parseObject(rich88ApiResponseData.getData().toString());
            responseData.setPathUrl(urlJson.getString("url"));
            return Result.success(responseData);
        } else {
            return errorCode(rich88ApiResponseData.getCode().toString(), rich88ApiResponseData.getMsg());
        }
    }

    /**
     * 创建RICH88 账号
     * 、
     *
     * @param cptOpenMember、 cptOpenMember
     * @return Rich88ApiResponseData
     */
    private Rich88ApiResponseData createRich88Member(CptOpenMember cptOpenMember, String platform, String lang) {

        Rich88ApiResponseData rich88ApiResponseData = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("account", cptOpenMember.getUserName());
            if (StringUtils.isNoneBlank(platform)) {
//                params.put("game_code", platform);
            }
//            params.put("entry", "");  //
            if (StringUtils.isNoneBlank(lang)) {
                params.put("lang", lang);
            }
            rich88ApiResponseData = commonRequest(getLoginRich88Url(), params, cptOpenMember.getUserId());
        } catch (Exception e) {
            logger.error("rich88log createT9Member:{}", e);
            e.printStackTrace();
        }
        return rich88ApiResponseData;

    }

    /**
     * 公共请求
     */
    private Rich88ApiResponseData commonRequest(String apiUrl, Map<String, Object> params, Object userId) {
        logger.info("rich88log  commonRequest userId:{},paramsMap:{}", userId, params);
        Map<String, String> header = new HashMap<>();
        long timestamp = System.currentTimeMillis();
        header.put("Content-Type", "application/json");
        header.put("accept", "application/json");
        header.put("api_key", getApiKey(timestamp));
        header.put("pf_id", OpenAPIProperties.RICH_PF_ID);
        header.put("timestamp", String.valueOf(timestamp));

        String json = GameUtil.postJson(apiUrl, params, header);
        Rich88ApiResponseData rich88ApiResponseData = JSONObject.parseObject(json, Rich88ApiResponseData.class);
        return rich88ApiResponseData;
    }

    /**
     * 获取请求 header api key
     *
     * @param timestamp timestamp
     * @return String
     */
    private String getApiKey(long timestamp) {
        StringBuilder checkValue = new StringBuilder();
        checkValue.append(OpenAPIProperties.RICH_PF_ID);
        checkValue.append(OpenAPIProperties.RICH_PRIVATE_KEY);
        checkValue.append(timestamp);

        return RichSHAEncrypt.getSHA256Str(checkValue.toString());
    }

    /**
     * 登录游戏，如果米有账号自动创建
     *
     * @return String
     */
    private String getLoginRich88Url() {
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.RICH_API_URL).append("/v2/platform/login");
        return builder.toString();
    }

    /**
     * 玩家退出游戏API地址
     *
     * @param account account
     * @return String
     */
    private String getLoginOutUrl(String account) {
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.RICH_API_URL).append("/v2/platform/logout/").append(account);
        return builder.toString();
    }

    public Result errorCode(String errorCode, String errorMessage) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        999 系統維護中
            case "999":
                return Result.failed("g000001", errorMessage);
//        10001 API 參數錯誤                                              No authorized to access
            case "10001":
                return Result.failed("g000007", errorMessage);

//        10002 執⾏過程錯誤                            Domain is null or the length of domain less than 2.
            case "10002":
                return Result.failed("g000005", errorMessage);

//        10003 API 訪問次數過於頻繁                                   Failed to pass the domain validation.
            case "10003":
                return Result.failed("g000008", errorMessage);

//        10004 時間格式錯誤。                     The encrypted data is null or the length of the encrypted data is equal to 0.
            case "10004":
                return Result.failed("g091024", errorMessage);

//        10005 時間區間超過10分鐘            Assertion(SAML) didn't pass the timestamp validation.
            case "10005":
                return Result.failed("g000006", errorMessage);

//        10006 時間區間超過5分鐘。                      Failed to extract the SAML parameters from the encrypted data.
            case "10006":
                return Result.failed("g000006", errorMessage);

//        10008 API 請求逾時。                                            Unknow action.
            case "10008":
                return Result.failed("g091067", errorMessage);

//        10009 平台未⽀援該功能                                    The same value as before.
            case "10009":
                return Result.failed("g100101", errorMessage);

//        10010 平台發⽣錯誤。                                                Time out.
            case "10010":
                return Result.failed("g009999", errorMessage);

//        10011 時間區間過長。                                            Read time out.
            case "10011":
                return Result.failed("g000006", errorMessage);

//        10012 時間區間過短。                                            Duplicate transactions.
            case "10012":
                return Result.failed("g000006", errorMessage);

//        11003 Primary key 衝突。                                          Please try again later.
            case "11003":
                return Result.failed("g100110", errorMessage);

//        11004 超過⼀定時間內可執⾏次數。                                            System is maintained.
            case "11004":
                return Result.failed("g000008", errorMessage);

            //        13001 超創建的玩家帳號重複                                           System is maintained.
            case "13001":
                return Result.failed("g100003", errorMessage);
            //        13002 帳號不存在                                           System is maintained.
            case "13002":
                return Result.failed("g010001", errorMessage);
            //        13004 此帳號已被阻擋                                       System is maintained.
            case "13004":
                return Result.failed("g200003", errorMessage);
            //        13005 帳號重複登入                                            System is maintained.
            case "13005":
                return Result.failed("g091032", errorMessage);
            //        13006 帳號已登出                                    System is maintained.
            case "13006":
                return Result.failed("g100103", errorMessage);
            //        14002 餘額不⾜                                            System is maintained.
            case "14002":
                return Result.failed("g300004", errorMessage);
            //        14005 ⾦錢轉入/轉出失敗                                           System is maintained.
            case "14005":
                return Result.failed("g300002", errorMessage);
            //        14006 ⾦錢結算失敗                                         System is maintained.
            case "14006":
                return Result.failed("g091018", errorMessage);
            //        14010 單⼀錢包餘額不⾜                                  System is maintained.
            case "14010":
                return Result.failed("g300004", errorMessage);
            //        17009 此帳號被禁⽌遊玩此遊戲                                           System is maintained.
            case "17009":
                return Result.failed("g200003", errorMessage);
            //        18004 離開遊戲失敗                                       System is maintained.
            case "18004":
                return Result.failed("g091092", errorMessage);
            //        20008 參數錯誤                                 System is maintained.
            case "20008":
                return Result.failed("g000007", errorMessage);
            //        11004 超過⼀定時間內可執⾏次數。

//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", errorMessage);
        }
    }
}
