package com.indo.game.service.cq.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.encrypt.Base64;
import com.indo.common.utils.encrypt.MD5;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.dto.ae.AeApiResponseData;
import com.indo.game.pojo.dto.ae.cq.CqApiResponseData;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.ae.AeService;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.cq.CqService;

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
 * AE电子
 *
 * @author
 */
@Service
public class CqServiceImpl implements CqService {

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
    public Result cqGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("cqlog {} aeGame account:{}, aeCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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
            logger.info("站点AE余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004", "会员余额不足");
        }
        try {

            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setUserId(loginUser.getId().intValue());
                //储存session
                cptOpenMember.setPassword(SnowflakeId.generateId().toString());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                externalService.saveCptOpenMember(cptOpenMember);
            } else {
                CptOpenMember updateCptOpenMember = new CptOpenMember();
                updateCptOpenMember.setId(cptOpenMember.getId());
                updateCptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(updateCptOpenMember);
            }
            Result result = this.logout(loginUser, parentName, ip);
            if (null != result && "00000".equals(result.getCode())) {
                //登录
                return initGame(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    /**
     * 登录逻辑
     */
    private Result initGame(GameParentPlatform platformGameParent, GamePlatform gamePlatform,
                            CptOpenMember cptOpenMember, String isMobileLogin) {
        CqApiResponseData cqApiResponseData = gameLogin(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin);
        if (null == cqApiResponseData) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        JSONObject jsonStatusObject = JSON.parseObject(cqApiResponseData.getStatus());
        if (("0").equals(jsonStatusObject.getString("code"))) {
            ApiResponseData responseData = new ApiResponseData();
            JSONObject jsonDataObject = JSON.parseObject(cqApiResponseData.getData());
            responseData.setPathUrl(jsonDataObject.getString("url"));
            return Result.success(cqApiResponseData);
        } else {
            return Result.failed("g091088", "第三方响应异常！");
        }
    }

    /**
     * 调用API登录
     */
    private CqApiResponseData gameLogin(GameParentPlatform platformGameParent, GamePlatform gamePlatform, CptOpenMember cptOpenMemberm, String isMobileLogin) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("account", cptOpenMemberm.getUserName());
        params.put("lang", platformGameParent.getLanguageType());
        params.put("session", platformGameParent.getCurrencyType());
        CqApiResponseData cqApiResponseData = null;
        try {
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.CQ_API_URL).append("/gameboy/player/sw/lobbylink");
            cqApiResponseData = commonRequest(apiUrl.toString(), params, cptOpenMemberm.getUserId(), "cqGameLogin");
        } catch (Exception e) {
            logger.error("cqlog aeGameLogin:{}", e);
            e.printStackTrace();
        }
        return cqApiResponseData;
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
            Map<String, String> params = new HashMap<String, String>();
            params.put("account", loginUser.getAccount());
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.AE_API_URL).append("/gameboy/player/logout");
            CqApiResponseData cqApiResponseData = commonRequest(apiUrl.toString(), params, loginUser.getId().intValue(), "cqGameLogin");
            if (null == cqApiResponseData) {
                return Result.failed();
            }
            JSONObject jsonObject = JSON.parseObject(cqApiResponseData.getStatus());
            if ("0".equals(jsonObject.getString("code"))) {
                return Result.success(cqApiResponseData);
            } else {
                return Result.failed();
            }
        } catch (Exception e) {
            logger.error("cqlog cqlogout:{}", e);
            e.printStackTrace();
            return Result.failed();
        }

    }


    /**
     * 公共请求
     */
    public CqApiResponseData commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        logger.info("cqlog {} commonRequest userId:{},paramsMap:{}", userId, params);
        CqApiResponseData cqApiResponseData = null;
        String resultString = GameUtil.doProxyPostHeaderJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId, OpenAPIProperties.CQ_API_TOKEN);
        logger.info("cqlog apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            cqApiResponseData = JSONObject.parseObject(resultString, CqApiResponseData.class);
            logger.info("cqlog {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, params, resultString, JSONObject.toJSONString(cqApiResponseData));
        }
        return cqApiResponseData;
    }

}
