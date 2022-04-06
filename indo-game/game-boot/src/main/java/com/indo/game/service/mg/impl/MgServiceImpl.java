package com.indo.game.service.mg.impl;

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
        logger.info("mgLog  {} mgGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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
                createMemberGame(cptOpenMember);
            } else {
                CptOpenMember updateCptOpenMember = new CptOpenMember();
                updateCptOpenMember.setId(cptOpenMember.getId());
                updateCptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(updateCptOpenMember);
            }

            JSONObject jsonObject = gameLogin(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin);
            if (!("201").equals(jsonObject.getString("code"))) {
                return errorCode(jsonObject.getString("code"), jsonObject.getString("message"));
            }
            //登录
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(jsonObject.getString("url"));
            return Result.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }


    /**
     * 创建账户并登录逻辑
     */
    private Result createMemberGame(CptOpenMember cptOpenMember) {
        JSONObject pgApiResponseData = createMember(cptOpenMember);
        if (null == pgApiResponseData) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        if (("201").equals(pgApiResponseData.getString("code"))) {
            externalService.saveCptOpenMember(cptOpenMember);
            return Result.success();
        } else {
            return errorCode(pgApiResponseData.getString("code"), pgApiResponseData.getString("message"));
        }
    }

    private JSONObject createMember(CptOpenMember cptOpenMember) {
        Map<String, String> map = new HashMap<>();
        map.put("playerId", cptOpenMember.getUserName());
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.MG_API_URL).append("/agents/")
                .append(OpenAPIProperties.MG_AGENT_CODE).append("/players");
        JSONObject apiResponseData = null;
        try {
            apiResponseData = commonRequest(builder.toString(), map, cptOpenMember.getUserId(), "createMgMember");
        } catch (Exception e) {
            logger.error("mgLog pgCeateMember:{}", e);
            e.printStackTrace();
        }
        return apiResponseData;

    }


    /**
     * 调用API登录
     */
    private JSONObject gameLogin(GameParentPlatform platformGameParent, GamePlatform gamePlatform, CptOpenMember cptOpenMemberm, String isMobileLogin) {
        JSONObject apiResponseData = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("contentCode", gamePlatform.getPlatformCode());
            if ("0".equals(isMobileLogin)) {
                params.put("platform", "Unknown");
            } else if ("1".equals(isMobileLogin)) {
                params.put("platform", "Mobile");
            } else {
                params.put("platform", "Unknown");
            }
            params.put("langCode ", platformGameParent.getLanguageType());
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.MG_SESSION_URL).append("/agents/").append(OpenAPIProperties.MG_AGENT_CODE);
            apiUrl.append("/players/").append(cptOpenMemberm.getUserName()).append("/sessions");
            apiResponseData = commonRequest(apiUrl.toString(), params, cptOpenMemberm.getUserId(), "mgGameLogin");
        } catch (Exception e) {
            logger.error("mglog mgGameLogin:{}", e);
            e.printStackTrace();
        }
        return apiResponseData;
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
    public JSONObject commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        logger.info("mgLog  {} commonRequest userId:{},paramsMap:{}", userId, params);
        JSONObject apiResponseData = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId);
        logger.info("mgLog  apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            apiResponseData = JSONObject.parseObject(resultString);
            logger.info("mgLog  {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
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
        }else if ("500".equals(errorCode)) {
            return Result.failed("g091145", errorMessage);
        }else {
            return Result.failed("g009999", errorMessage);
        }
    }
}
