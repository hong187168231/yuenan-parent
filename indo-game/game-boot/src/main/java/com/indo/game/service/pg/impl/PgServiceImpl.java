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
import com.indo.game.pojo.dto.cq.CqApiResponseData;
import com.indo.game.pojo.dto.pg.PgApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
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
     * 登录游戏CQ9游戏
     *
     * @return loginUser 用户信息
     */
    @Override
    public Result pgGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("pglog  {} pgGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
        // 是否开售校验
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == platformGameParent) {
            return Result.failed("(" + parentName + ")游戏平台不存在");
        }
        if ("0".equals(platformGameParent.getIsStart())) {
            return Result.failed("g" + "100101", "游戏平台未启用");
        }
        if ("1".equals(platformGameParent.getIsOpenMaintenance())) {
            return Result.failed("g000001", platformGameParent.getMaintenanceContent());
        }
        GamePlatform gamePlatform = new GamePlatform();
        if (!platform.equals(parentName)) {
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
                createMemberGame(platformGameParent, gamePlatform, ip, cptOpenMember, isMobileLogin);
            } else {
                CptOpenMember updateCptOpenMember = new CptOpenMember();
                updateCptOpenMember.setId(cptOpenMember.getId());
                updateCptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(updateCptOpenMember);
            }

            StringBuilder builder = new StringBuilder();
            builder.append(OpenAPIProperties.PG_API_URL).append("/web-lobby/games/?");
            builder.append("operator_token=").append(OpenAPIProperties.PG_API_TOKEN);
            builder.append("&operator_player_session=").append(cptOpenMember.getPassword());
            builder.append("&language=").append(platformGameParent.getCurrencyType());
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
        if (null == pgApiResponseData || null == pgApiResponseData.getData()) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        JSONObject jsonDataObject = JSON.parseObject(pgApiResponseData.getData());
        JSONObject jsonStatusObject = JSON.parseObject(pgApiResponseData.getError());
        if (("1").equals(jsonDataObject.getString("action_result"))) {
            externalService.saveCptOpenMember(cptOpenMember);
            return Result.success();
        } else {
            return errorCode(jsonStatusObject.getString("code"), jsonStatusObject.getString("message"));
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
            pgApiResponseData = commonRequest(builder.toString(), map, cptOpenMember.getUserId(), "createPgMember");
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
            PgApiResponseData pgApiResponseData = commonRequest(builder.toString(), map, loginUser.getId().intValue(), "cqGameLogin");
            if (null == pgApiResponseData) {
                return Result.failed();
            }
            JSONObject jsonDataObject = JSON.parseObject(pgApiResponseData.getData());
            JSONObject jsonStatusObject = JSON.parseObject(pgApiResponseData.getError());
            if (("1").equals(jsonDataObject.getString("action_result"))) {
                return Result.success();
            } else {
                return errorCode(jsonStatusObject.getString("code"), jsonStatusObject.getString("message"));
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
