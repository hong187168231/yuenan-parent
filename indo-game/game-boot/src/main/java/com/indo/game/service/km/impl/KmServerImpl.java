package com.indo.game.service.km.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.km.KmService;

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
public class KmServerImpl implements KmService {

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
    public Result kmGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("kmLog  {} kmGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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

            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(SnowflakeId.generateId().toString());
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                externalService.saveCptOpenMember(cptOpenMember);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                logout(loginUser, platform, ip);
            }
            JSONObject tokenJson = gameToken(loginUser, gameParentPlatform, ip);
            if (StringUtils.isEmpty(tokenJson.getString("authtoken"))) {
                return errorCode(tokenJson.getString("code"), tokenJson.getString("message"));
            }
            StringBuilder builder = new StringBuilder();
            if(OpenAPIProperties.KM_IS_PLATFORM_LOGIN.equals("Y")) {
//                1：手机 0:PC
                builder.append(OpenAPIProperties.KM_GAME_URL);
                if("0".equals(isMobileLogin)){
                    builder.append("/"+OpenAPIProperties.KM_DESKTOP+"?");
                }else {
                    builder.append("/"+OpenAPIProperties.KM_MOBILE+"?");
                }
                builder.append("token=").append(tokenJson.getString("authtoken"));
            }else {
                builder.append(OpenAPIProperties.KM_GAME_URL).append("/gamelauncher?");
                builder.append("gpcode=").append("KMQM");
                builder.append("&gcode=").append(gamePlatform.getPlatformCode());
                builder.append("&token=").append(tokenJson.getString("authtoken"));
                builder.append("&lang=").append(gameParentPlatform.getLanguageType());
            }

            //登录
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(builder.toString());
            logger.error("kmLog  kmGame玩家登录请求地址 url:{}", builder.toString());
            return Result.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    private JSONObject gameToken(LoginInfo loginUser, GameParentPlatform platformGameParent, String ip) {
        Map<String, String> map = new HashMap<>();
        map.put("ipaddress", ip);
        map.put("username", loginUser.getAccount());
        map.put("userid", loginUser.getAccount());
        map.put("lang", platformGameParent.getLanguageType());
        map.put("cur", platformGameParent.getCurrencyType());
        map.put("betlimitid", "1");
        map.put("platformtype", "1");
        //调用此API时，请务必传递正确的'istestplayer'参数值。若针对玩家传递'istestplayer=false'，那么玩家就会
//        在QM中以真实玩家身份被创建，且无法经由API将此身份变更为测试玩家。
        map.put("istestplayer", "false");
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.KM_API_URL).append("/api/player/authorize");
        JSONObject apiResponseData = null;
        try {
            logger.error("kmLog  gameToken请求 url:{},params:{},", builder.toString(),map);
            apiResponseData = commonRequest(builder.toString(), map, loginUser.getId().intValue(), "createKmToken");
            logger.error("kmLog  gameToken返回 apiResponseData:{},", JSONObject.toJSONString(apiResponseData));
        } catch (Exception e) {
            logger.error("kmLog pgCeateMember:{}", e);
            e.printStackTrace();
        }
        return apiResponseData;

    }


    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("userid", loginUser.getAccount());
            StringBuilder builder = new StringBuilder();
            builder.append(OpenAPIProperties.KM_API_URL).append("/api/player/deauthorize");
            JSONObject apiResponseData = null;
            logger.error("kmLog  logout请求 url:{},params:{},", builder.toString(),map);
            apiResponseData = commonRequest(builder.toString(), map, loginUser.getId().intValue(), "logout");
            logger.error("kmLog  logout返回 apiResponseData:{},", JSONObject.toJSONString(apiResponseData));
            if (apiResponseData.getBoolean("success")) {
                return Result.success();
            }else {
                return Result.failed(apiResponseData.getString("desc"));
            }

        } catch (Exception e) {
            logger.error("kmLog  kmLog out:{}", e);
            e.printStackTrace();
            return Result.failed();
        }

    }


    /**
     * 公共请求
     */
    public JSONObject commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        logger.info("kmLog  {} commonRequest userId:{},paramsMap:{}", userId, params);
        JSONObject apiResponseData = null;
        String resultString = GameUtil.doProxyPostKmJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId, OpenAPIProperties.KM_CLIENT_ID, OpenAPIProperties.KM_CLIENT_SECRET);
        logger.info("kmLog  apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            apiResponseData = JSONObject.parseObject(resultString);
            logger.info("kmLog  {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, params, resultString, JSONObject.toJSONString(apiResponseData));
        }
        return apiResponseData;
    }


    public Result errorCode(String errorCode, String errorMessage) {
        if ("401".equals(errorCode)) {
            return Result.failed("g000002", errorMessage);
        } else if ("404".equals(errorCode)) {
            return Result.failed("g009999", errorMessage);
        } else if ("409".equals(errorCode)) {
            return Result.failed("g009999", errorMessage);
        } else if ("400".equals(errorCode)) {
            return Result.failed("g000007", errorMessage);
        } else if ("500".equals(errorCode)) {
            return Result.failed("g091145", errorMessage);
        } else {
            return Result.failed("g009999", errorMessage);
        }
    }
}
