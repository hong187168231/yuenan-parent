package com.indo.game.service.mg.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.mg.MgService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * MG
 *
 * @author
 */
@Service
public class MgServiceImpl implements MgService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private GameLogoutService gameLogoutService;

    /**
     * 登录游戏Mg游戏
     *
     * @return loginUser 用户信息
     */
    @Override
    public Result mgGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("mgLog登录游戏Mg游戏 mgGame userId:{}, account:{}, platform:{}, parentName:{}", loginUser.getId(), loginUser.getAccount(), platform, parentName);
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
//        //验证站点棋牌余额
//        if (null == balance || BigDecimal.ZERO == balance) {
//            logger.info("站点PG余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
        try {
            JSONObject tokenJson;
            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                tokenJson = gameToken(loginUser.getId().intValue());
                if (StringUtils.isEmpty(tokenJson.getString("access_token"))) {
                    return errorCode(tokenJson.getString("code"), tokenJson.getString("message"),countryCode);
                }
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(SnowflakeId.generateId().toString());
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return createMemberGame(gameParentPlatform , gamePlatform,cptOpenMember, isMobileLogin, "Bearer "+tokenJson.getString("access_token"), countryCode);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
//                logout(loginUser, platform, ip,countryCode);
                tokenJson = gameToken(loginUser.getId().intValue());
                if (StringUtils.isEmpty(tokenJson.getString("access_token"))) {
                    return errorCode(tokenJson.getString("code"), tokenJson.getString("message"),countryCode);
                }
            }

            return gameLogin(gameParentPlatform, gamePlatform, cptOpenMember, isMobileLogin, "Bearer "+tokenJson.getString("access_token"), countryCode);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    private JSONObject gameToken(Integer userId) {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", OpenAPIProperties.MG_CLIENT_ID);
        map.put("client_secret", OpenAPIProperties.MG_CLIENT_SECRET);
        map.put("grant_type", "client_credentials");
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.MG_TOKEN_URL).append("/connect/token");
        JSONObject apiResponseData = null;
        logger.info("mgLog登录游戏Mg游戏gameToken输入 builder:{}, params:{}, userId:{}", builder.toString(), map, userId);
        try {
            apiResponseData = commonRequest(builder.toString(), map, userId, "", "createMgToken");
            logger.info("mgLog登录游戏Mg游戏gameToken返回 JSONObject:{}", null!=apiResponseData?apiResponseData.toJSONString():"");
        } catch (Exception e) {
            logger.error("mgLog gameToken:{}", e);
            e.printStackTrace();
        }
        return apiResponseData;

    }


    /**
     * 创建账户并登录逻辑
     */
    private Result createMemberGame(GameParentPlatform platformGameParent, GamePlatform gamePlatform,CptOpenMember cptOpenMember, String isMobileLogin, String token,String countryCode) {
        JSONObject pgApiResponseData = createMember(cptOpenMember, token);
        if (null == pgApiResponseData) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }
        if (!StringUtils.isEmpty(pgApiResponseData.getString("uri"))) {
            externalService.saveCptOpenMember(cptOpenMember);
            return gameLogin(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin, "Bearer "+token, countryCode);
        } else {
            if (!StringUtils.isEmpty(pgApiResponseData.getString("error"))) {
                JSONObject errorJ = (JSONObject)pgApiResponseData.get("error");
                return errorCode(errorJ.getString("code"), errorJ.getString("message"),countryCode);
            }else {
                return errorCode(pgApiResponseData.getString("code"), pgApiResponseData.getString("message"),countryCode);
            }
        }
    }

    private JSONObject createMember(CptOpenMember cptOpenMember, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("playerId", cptOpenMember.getUserName());
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.MG_API_URL).append("/api/v1/agents/")
                .append(OpenAPIProperties.MG_AGENT_CODE).append("/players");
        logger.info("mgLog登录游戏Mg游戏创建用户createMember输入 url:{}, params:{}, userId:{}, token:{}", builder.toString(), map, cptOpenMember.getUserId(),token);
        JSONObject apiResponseData = null;
        try {
            apiResponseData = commonRequest(builder.toString(), map, cptOpenMember.getUserId(), token, "createMgMember");
            logger.info("mgLog登录游戏Mg游戏创建用户createMember返回 JSONObject:{}", null!=apiResponseData?apiResponseData.toJSONString():"");
        } catch (Exception e) {
            logger.error("mgLog pgCeateMember:{}", e);
            e.printStackTrace();
        }
        return apiResponseData;

    }


    /**
     * 调用API登录
     */
    private Result gameLogin(GameParentPlatform platformGameParent, GamePlatform gamePlatform,
                                 CptOpenMember cptOpenMemberm, String isMobileLogin, String token,String countryCode) {
        JSONObject apiResponseData = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            if(OpenAPIProperties.MG_IS_PLATFORM_LOGIN.equals("Y")){
//                1	Live	真人视讯
//                2	Slots	老虎机
//                3	Sports	体育
//                4	Animal	斗鸡
//                5	Poker	棋牌游戏
//                6	Fishing	捕鱼
//                7	Lottery	彩票
                String contentCode = "Live Dealer";
                if(gamePlatform.getCategoryId()==1){
//                    contentCode = "Live Dealer";
                    contentCode = "SMG_titaniumLiveGames_BaccaratNC";
                }else if(gamePlatform.getCategoryId()==2){
//                    contentCode = "Slot";
                    contentCode = "Slot";
                }else if(gamePlatform.getCategoryId()==5){
//                    contentCode = "QiPai";
                    contentCode = "Slot";
                }else if(gamePlatform.getCategoryId()==6){
//                    contentCode = "Fishing";
                    contentCode = "Slot";
                }
                params.put("contentCode", contentCode);
            }else {
                params.put("contentCode", gamePlatform.getPlatformCode());
            }

            if ("0".equals(isMobileLogin)) {
                params.put("platform", "Desktop");
            } else if ("1".equals(isMobileLogin)) {
                params.put("platform", "Mobile");
            } else {
                params.put("platform", "Unknown");
            }
            //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
            String lang = "";
            if(null!=countryCode&&!"".equals(countryCode)){
                switch (countryCode) {
                    case "IN":
                        lang = "en-US";
                        break;
                    case "EN":
                        lang = "en-US";
                        break;
                    case "CN":
                        lang = "zh-CN";
                        break;
                    case "VN":
                        lang = "vi-VN";
                        break;
                    case "TW":
                        lang = "zh-TW";
                        break;
                    case "TH":
                        lang = "th-TH";
                        break;
                    case "ID":
                        lang = "in-ID";
                        break;
                    case "MY":
                        lang = "ms-MY";
                        break;
                    case "KR":
                        lang = "ko-KR";
                        break;
                    case "JP":
                        lang = "ja-JP";
                        break;
                    default:
                        lang = platformGameParent.getLanguageType();
                        break;
                }
            }else{
                lang = platformGameParent.getLanguageType();
            }
            params.put("langCode", lang);
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.MG_API_URL).append("/api/v1/agents/").append(OpenAPIProperties.MG_AGENT_CODE);
            apiUrl.append("/players/").append(cptOpenMemberm.getUserName()).append("/sessions");
            logger.info("mgLog登录游戏Mg游戏登录用户gameLogin输入 apiUrl:{}, params:{}, userId:{}, token:{}", apiUrl.toString(), params, cptOpenMemberm.getUserId(), token);
            apiResponseData = commonRequest(apiUrl.toString(), params, cptOpenMemberm.getUserId(), token, "mgGameLogin");
            logger.info("mgLog登录游戏Mg游戏登录用户gameLogin返回JSONObject:{}", null!=apiResponseData?apiResponseData.toJSONString():"");
            if (null != apiResponseData && null != apiResponseData.getString("url") && !"".equals(apiResponseData.getString("url"))) {
                //登录
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(apiResponseData.getString("url"));
                return Result.success(responseData);
            }else if(null == apiResponseData){
                return Result.failed();
            }else {
                if (!StringUtils.isEmpty(apiResponseData.getString("error"))) {
                    JSONObject errorJ = (JSONObject)apiResponseData.get("error");
                    return errorCode(errorJ.getString("code"), errorJ.getString("message"),countryCode);
                }else {
                    return errorCode(apiResponseData.getString("code"), apiResponseData.getString("message"),countryCode);
                }
            }

        } catch (Exception e) {
            logger.error("mglog mgGameLogin:{}", e);
            e.printStackTrace();
            return Result.failed();
        }
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(String account,String platform, String ip,String countryCode) {
        try {
            return Result.success();
        } catch (Exception e) {
            logger.error("mgLog  mgLog out:{}", e);
            e.printStackTrace();
            return Result.failed();
        }

    }


    /**
     * 公共请求
     */
    public JSONObject commonRequest(String apiUrl, Map<String, String> params, Integer userId, String token, String type) throws Exception {
        JSONObject apiResponseData = null;
         String resultString = GameUtil.doProxyPostHeaderJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId, token);
        if (StringUtils.isNotEmpty(resultString)) {
            apiResponseData = JSONObject.parseObject(resultString);
        }
        return apiResponseData;
    }


    public Result errorCode(String errorCode, String errorMessage,String countryCode) {
        switch (errorCode) {
            case "InputValidationError":
                return Result.failed("g091118", MessageUtils.get("g091118",countryCode));
            case "GeneralError":
                return Result.failed("g091145", MessageUtils.get("g091145",countryCode));
            case "PlayerDoesNotExist":
                return Result.failed("g010001", MessageUtils.get("g010001",countryCode));
            case "ContentNotFoundForPlayer":
                return Result.failed("g091068", MessageUtils.get("g091068",countryCode));
            case "PlayerIsLocked":
                return Result.failed("g200003", MessageUtils.get("g200003",countryCode));
            case "GameDoesNotExist":
                return Result.failed("g091158", MessageUtils.get("g091158",countryCode));
            //        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
