package com.indo.game.service.yl.impl;

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
import com.indo.game.service.ps.PsService;
import com.indo.game.service.yl.YlService;

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
public class YlServiceImpl implements YlService {

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
    public Result ylGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("yllog  {} YLGame account:{}, ylCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
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
            logger.info("站点YL余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004", "会员余额不足");
        }
        try {

            // 验证且绑定（YL-CPT第三方会员关系）
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
                cptOpenMember.setPassword(SnowflakeId.generateId().toString());
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                logout(loginUser, platform, ip);
            }
            //获取KEY
            String apiKey = initGame(platformGameParent, cptOpenMember);
            if (StringUtils.isEmpty(apiKey)) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            String path = gameLogin(apiKey, platform, platformGameParent, cptOpenMember);
            if (StringUtils.isEmpty(path)) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            //登录
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(path);
            return Result.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    private String gameLogin(String apiKey, String platform, GameParentPlatform platformGameParent, CptOpenMember cptOpenMember) {
        String pathUrl = "";
        try {
            Map<String, String> map = new HashMap<>();
            map.put("user", cptOpenMember.getUserId() + "");
            map.put("key", apiKey);
            map.put("extension1", OpenAPIProperties.YL_EXTENSION);
            map.put("userName", cptOpenMember.getUserName());
            map.put("language", platformGameParent.getLanguageType());
            if (!"YL".equals(platform)) {
                map.put("gameId", platform);
            }
            StringBuilder builder = new StringBuilder();
            builder.append(OpenAPIProperties.YL_API_URL).append("/api/").append(OpenAPIProperties.YL_WEB_SITE).append("/login");
            JSONObject jsonObject = commonRequest(builder.toString(), map, cptOpenMember.getUserId().intValue(), "yLGameLogin");
            logger.error("YL捕鱼登录请求返回：", jsonObject);
            if (null != jsonObject && "1".equals(jsonObject.getString("status"))) {
                pathUrl = jsonObject.getString("url");
            }
        } catch (Exception e) {
            logger.error("YL捕鱼登录获取异常：", e);
            e.printStackTrace();
        }
        return pathUrl;
    }

    private String initGame(GameParentPlatform platformGameParent, CptOpenMember cptOpenMember) {
        String apiKey = "";
        try {
            Map<String, String> map = new HashMap<>();
            map.put("cert", OpenAPIProperties.YL_CERT);
            map.put("user", cptOpenMember.getUserId() + "");
            map.put("userName", cptOpenMember.getUserName());
            map.put("extension1", OpenAPIProperties.YL_EXTENSION);
            map.put("currency", platformGameParent.getCurrencyType());
            StringBuilder builder = new StringBuilder();
            builder.append(OpenAPIProperties.YL_API_URL).append("/api/").append(OpenAPIProperties.YL_WEB_SITE).append("/getKey");
            JSONObject jsonObject = commonRequest(builder.toString(), map, cptOpenMember.getUserId().intValue(), "yLInitGame");
            logger.info("YL捕鱼获取KEY请求返回：", jsonObject);
            if (null != jsonObject && "1".equals(jsonObject.getString("status"))) {
                apiKey = jsonObject.getString("key");
            }
        } catch (Exception e) {
            logger.error("YL捕鱼获取KEY异常：", e);
            e.printStackTrace();
        }
        return apiKey;
    }


    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("cert", OpenAPIProperties.YL_CERT);
            map.put("user", loginUser.getId() + "");
            StringBuilder builder = new StringBuilder();
            builder.append(OpenAPIProperties.YL_API_URL).append("/api/").append(OpenAPIProperties.YL_WEB_SITE).append("/logout");
            JSONObject jsonObject = commonRequest(builder.toString(), map, loginUser.getId().intValue(), "yLLogOut");
            logger.info("YL捕鱼强迫登出玩家请求返回：", jsonObject);
            if (null != jsonObject && "1".equals(jsonObject.getString("status"))) {
                return Result.success();
            }
            return Result.failed();
        } catch (Exception e) {
            logger.error("yllog 强迫登出玩家异常:{}", e);
            e.printStackTrace();
            return Result.failed();
        }

    }


    /**
     * 公共请求
     */
    public JSONObject commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        logger.info("yllog  {} commonRequest userId:{},paramsMap:{}", userId, params);
        JSONObject psApiResponseData = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId);
        logger.info("yllog  apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            psApiResponseData = JSONObject.parseObject(resultString);
            logger.info("yllog  {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, params, resultString, JSONObject.toJSONString(psApiResponseData));
        }
        return psApiResponseData;
    }

}
