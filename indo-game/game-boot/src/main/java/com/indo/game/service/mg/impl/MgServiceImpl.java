package com.indo.game.service.mg.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.GameUtil;
import com.indo.game.common.util.RandomGUID;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.pg.PgApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.mg.MgService;
import com.indo.game.service.pg.PgService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
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


    /**
     * 登录游戏Mg游戏
     *
     * @return loginUser 用户信息
     */
    @Override
    public Result mgGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("mgLog登录游戏Mg游戏 mgGame userId:{}, account:{}, platform:{}, parentName:{}", loginUser.getId(), loginUser.getAccount(), platform, parentName);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("(" + parentName + ")平台不存在");
        }
        if (0==gameParentPlatform.getIsStart()) {
            return Result.failed("g100101", "平台未启用");
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", gameParentPlatform.getMaintenanceContent());
        }
        // 是否开售校验
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
        if (null == gamePlatform) {
            return Result.failed("(" + platform + ")游戏不存在");
        }
        if (0==gamePlatform.getIsStart()) {
            return Result.failed("g100102", "游戏未启用");
        }
        if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
            return Result.failed("g091047", gamePlatform.getMaintenanceContent());
        }
        BigDecimal balance = loginUser.getBalance();
        //验证站点棋牌余额
        if (null == balance || BigDecimal.ZERO == balance) {
            logger.info("站点PG余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004", "会员余额不足");
        }
        try {
            JSONObject tokenJson;
            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                tokenJson = gameToken(loginUser.getId().intValue());
                if (StringUtils.isEmpty(tokenJson.getString("access_token"))) {
                    return errorCode(tokenJson.getString("code"), tokenJson.getString("message"));
                }
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(SnowflakeId.generateId().toString());
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return createMemberGame(gameParentPlatform , gamePlatform,cptOpenMember, isMobileLogin, "Bearer "+tokenJson.getString("access_token"));
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                logout(loginUser, platform, ip);
                tokenJson = gameToken(loginUser.getId().intValue());
                if (StringUtils.isEmpty(tokenJson.getString("access_token"))) {
                    return errorCode(tokenJson.getString("code"), tokenJson.getString("message"));
                }
            }

            return gameLogin(gameParentPlatform, gamePlatform, cptOpenMember, isMobileLogin, "Bearer "+tokenJson.getString("access_token"));

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
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
    private Result createMemberGame(GameParentPlatform platformGameParent, GamePlatform gamePlatform,CptOpenMember cptOpenMember, String isMobileLogin, String token) {
        JSONObject pgApiResponseData = createMember(cptOpenMember, token);
        if (null == pgApiResponseData) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        if (!StringUtils.isEmpty(pgApiResponseData.getString("uri"))) {
            externalService.saveCptOpenMember(cptOpenMember);
            return gameLogin(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin, "Bearer "+token);
        } else {
            if (!StringUtils.isEmpty(pgApiResponseData.getString("error"))) {
                JSONObject errorJ = (JSONObject)pgApiResponseData.get("error");
                return errorCode(errorJ.getString("code"), errorJ.getString("message"));
            }else {
                return errorCode(pgApiResponseData.getString("code"), pgApiResponseData.getString("message"));
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
                                 CptOpenMember cptOpenMemberm, String isMobileLogin, String token) {
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
                    contentCode = "Live Dealer";
                }else if(gamePlatform.getCategoryId()==2){
                    contentCode = "Slot";
                }else if(gamePlatform.getCategoryId()==5){
                    contentCode = "QiPai";
                }else if(gamePlatform.getCategoryId()==6){
                    contentCode = "Fishing";
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
            params.put("langCode", platformGameParent.getLanguageType());
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
                    return errorCode(errorJ.getString("code"), errorJ.getString("message"));
                }else {
                    return errorCode(apiResponseData.getString("code"), apiResponseData.getString("message"));
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
    public Result logout(LoginInfo loginUser, String platform, String ip) {
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


    public Result errorCode(String errorCode, String errorMessage) {
        switch (errorCode) {
            case "InputValidationError":
                return Result.failed("g091118", errorMessage);
            case "GeneralError":
                return Result.failed("g091145", errorMessage);
            case "PlayerDoesNotExist":
                return Result.failed("g010001", errorMessage);
            case "ContentNotFoundForPlayer":
                return Result.failed("g091068", errorMessage);
            case "PlayerIsLocked":
                return Result.failed("g200003", errorMessage);
            case "GameDoesNotExist":
                return Result.failed("g091158", errorMessage);
            //        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", errorMessage);
        }
    }
}
