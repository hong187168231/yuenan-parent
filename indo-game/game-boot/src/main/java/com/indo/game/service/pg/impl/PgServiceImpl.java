package com.indo.game.service.pg.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.game.common.util.RandomGUID;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.pg.PgApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.service.game.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
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
 * PG
 *
 * @author
 */
@Service
public class PgServiceImpl implements PgService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;


    /**
     * 登录游戏PG游戏
     *
     * @return loginUser 用户信息
     */
    @Override
    public Result pgGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("pglog  {} pgGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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

            StringBuilder builder = new StringBuilder();
            if("Y".equals(OpenAPIProperties.PG_IS_PLATFORM_LOGIN)) {
                builder.append(OpenAPIProperties.PG_LOGIN_URL).append("/web-lobby/games/?");
                builder.append("operator_token=").append(OpenAPIProperties.PG_API_TOKEN);
                builder.append("&operator_player_session=").append(cptOpenMember.getPassword());
                builder.append("&language=").append(gameParentPlatform.getLanguageType());
            }else {
                builder.append(OpenAPIProperties.PG_LOGIN_URL).append("/"+gamePlatform.getPlatformCode()+"/index.html?");
                builder.append("btt=1");//游戏启动模式3
                builder.append("&ot=").append(OpenAPIProperties.PG_API_TOKEN);//运营商独有的身份识别
                builder.append("&ops=").append(cptOpenMember.getPassword());//运营商系统生成的令牌
                builder.append("&language=").append(gameParentPlatform.getLanguageType());//游戏显示语言
            }
            logger.info("pglog  pgGame登录玩家 urlapi:{}", builder.toString(),cptOpenMember);
            //登录
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(builder.toString());
            return Result.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }


    /**
     * 创建账户并登录逻辑
     */
    private Result createMemberGame(GameParentPlatform platformGameParent, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember, String isMobileLogin) {
        PgApiResponseData pgApiResponseData = createMember(platformGameParent, gamePlatform, ip, cptOpenMember, isMobileLogin);
        if (null == pgApiResponseData ) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        JSONObject jsonDataObject = null;
        JSONObject jsonStatusObject = null;
        if(null!=pgApiResponseData.getData()){
            jsonDataObject = JSON.parseObject(pgApiResponseData.getData());
        }
        if(null!=pgApiResponseData.getError()){
            jsonStatusObject = JSON.parseObject(pgApiResponseData.getError());
        }
        if (null!=jsonDataObject&&("1").equals(jsonDataObject.getString("action_result"))) {
            externalService.saveCptOpenMember(cptOpenMember);
            return Result.success();
        } else {
            if(null!=jsonStatusObject){
                return errorCode(jsonStatusObject.getString("code"), jsonStatusObject.getString("message"));
            }else {
                return Result.failed("g091087", "第三方请求异常！");
            }
        }
    }


    private PgApiResponseData createMember(GameParentPlatform platformGameParent, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember, String isMobileLogin) {
        Map<String, String> map = new HashMap<>();
        map.put("operator_token", OpenAPIProperties.PG_API_TOKEN);
        map.put("secret_key", OpenAPIProperties.PG_SECRET_KEY);
        map.put("player_name", cptOpenMember.getUserName());
        map.put("currency", platformGameParent.getCurrencyType());
        RandomGUID myGUID = new RandomGUID();
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.PG_API_URL).append("/Player/v1/Create")
                .append("?trace_id=").append(myGUID.toString());
        PgApiResponseData pgApiResponseData = null;
        try {
            logger.info("pglog  createMember创建玩家 urlapi:{},paramsMap:{},loginUser:{}", builder.toString(), map,cptOpenMember);
            pgApiResponseData = commonRequest(builder.toString(), map, cptOpenMember.getUserId(), "createPgMember");
            logger.info("pglog  createMember创建玩家返回 pgApiResponseData:{}", JSONObject.toJSONString(pgApiResponseData));
        } catch (Exception e) {
            logger.error("pglog pgCeateMember:{}", e);
            e.printStackTrace();
        }
        return pgApiResponseData;

    }


    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        try {
            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(platform);
            if (null == platformGameParent) {
                return Result.failed();
            }
            Map<String, String> map = new HashMap<>();
            map.put("operator_token", OpenAPIProperties.PG_API_TOKEN);
            map.put("player_name", loginUser.getAccount());
            map.put("secret_key", OpenAPIProperties.PG_SECRET_KEY);
            map.put("currency", platformGameParent.getCurrencyType());
            StringBuilder builder = new StringBuilder();
            RandomGUID myGUID = new RandomGUID();
            builder.append(OpenAPIProperties.PG_API_URL).append("/Player/v1/Kick")
                    .append("?trace_id=").append(myGUID.toString());
            logger.info("pglog  logout登出玩家 urlapi:{},paramsMap:{},loginUser:{}", builder.toString(), map,loginUser);
            PgApiResponseData pgApiResponseData = commonRequest(builder.toString(), map, loginUser.getId().intValue(), "cqGameLogin");
            logger.info("pglog  logout登出玩家返回 pgApiResponseData:{}", JSONObject.toJSONString(pgApiResponseData));
            if (null == pgApiResponseData) {
                return Result.failed();
            }
            JSONObject jsonDataObject = null;
            JSONObject jsonStatusObject = null;
            if(null!=pgApiResponseData.getData()){
                jsonDataObject = JSON.parseObject(pgApiResponseData.getData());
            }
            if(null!=pgApiResponseData.getError()){
                jsonStatusObject = JSON.parseObject(pgApiResponseData.getError());
            }
            if (null!=jsonDataObject&&("1").equals(jsonDataObject.getString("action_result"))) {
                return Result.success();
            } else {
                if(null!=jsonStatusObject){
                    return errorCode(jsonStatusObject.getString("code"), jsonStatusObject.getString("message"));
                }else {
                    return Result.failed("g091087", "第三方请求异常！");
                }
            }
        } catch (Exception e) {
            logger.error("pglog  pglog out:{}", e);
            e.printStackTrace();
            return Result.failed();
        }

    }


    /**
     * 公共请求
     */
    public PgApiResponseData commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        logger.info("pglog  {} commonRequest userId:{},paramsMap:{}", userId, params);
        PgApiResponseData cqApiResponseData = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId);
        logger.info("pglog  apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            cqApiResponseData = JSONObject.parseObject(resultString, PgApiResponseData.class);
            logger.info("pglog  {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, params, resultString, JSONObject.toJSONString(cqApiResponseData));
        }
        return cqApiResponseData;
    }


    public Result errorCode(String errorCode, String errorMessage) {
        if ("1034".equals(errorCode)) {
            return Result.failed("g091087", errorMessage);
        } else if ("1035".equals(errorCode)) {
            return Result.failed("g009999", errorMessage);
        } else if ("1200".equals(errorCode)) {
            return Result.failed("g091124", errorMessage);
        } else if ("1204".equals(errorCode)) {
            return Result.failed("g091008", errorMessage);
        } else if ("1305".equals(errorCode)) {
            return Result.failed("g100003", errorMessage);
        } else {
            return Result.failed("g009999", errorMessage);
        }
    }
}
