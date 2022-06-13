package com.indo.game.service.bl.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.RandomUtil;
import com.indo.common.utils.SignMd5Utils;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.bl.BlService;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;

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
 * BOLE
 *
 * @author
 */
@Service
public class BlServiceImpl implements BlService {

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
    public Result blGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("BLlog  {} dgGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("(" + parentName + ")游戏平台不存在");
        }
        if (0==gameParentPlatform.getIsStart()) {
            return Result.failed("g100101", "游戏平台未启用");
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", gameParentPlatform.getMaintenanceContent());
        }
        if (!platform.equals(parentName)) {
            GamePlatform gamePlatform;
            // 是否开售校验
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
            if (null == gamePlatform) {
                return Result.failed("(" + platform + ")平台游戏不存在");
            }
            if (0==gamePlatform.getIsStart()) {
                return Result.failed("g100102", "游戏未启用");
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

            JSONObject apiResponseData = gameLogin(gameParentPlatform, cptOpenMember);
            if (null == apiResponseData || !"200".equals(apiResponseData.getJSONObject("resp_msg").getString("code"))) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            //登录
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(apiResponseData.getJSONObject("resp_data").getString("url"));
            return Result.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    private JSONObject gameLogin(GameParentPlatform platformGameParent, CptOpenMember cptOpenMember) {
        Map<String, String> map = new HashMap<String, String>();
        Integer random = RandomUtil.getRandomOne(7);
        Long dataTime = System.currentTimeMillis() / 1000;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OpenAPIProperties.BL_KEY_SECRET).append(random).append(dataTime);
        String sign = SignMd5Utils.getSha1(stringBuilder.toString()).toLowerCase();
        map.put("player_account", cptOpenMember.getUserName());
        map.put("lang", platformGameParent.getLanguageType());
        map.put("ip", "116.204.208.100");
        map.put("country", platformGameParent.getLanguageType());
        map.put("AccessKeyId", OpenAPIProperties.BL_KEY_ID);
        map.put("Timestamp", dataTime + "");
        map.put("Nonce", random + "");
        map.put("Sign", sign);
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(OpenAPIProperties.BL_API_URL).append("/v1/player/login");
        JSONObject dgApiResponseData = null;
        try {
            dgApiResponseData = commonRequest(apiUrl.toString(), map, cptOpenMember.getUserId(), "createBLlogin");
        } catch (Exception e) {
            logger.error("BLlog dgGameLoginr:{}", e);
            e.printStackTrace();
        }
        return dgApiResponseData;
    }


    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            Long dataTime =  System.currentTimeMillis() / 1000;
            Integer random = RandomUtil.getRandomOne(7);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(OpenAPIProperties.BL_KEY_SECRET).append(random).append(dataTime);
            String sign = SignMd5Utils.getSha1(stringBuilder.toString()).toLowerCase();
            map.put("player_account", loginUser.getAccount());
            map.put("AccessKeyId", OpenAPIProperties.BL_KEY_ID);
            map.put("Nonce", random + "");
            map.put("Sign", sign);
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.BL_API_URL).append("/v1/player/logout");
            JSONObject apiResponseData = commonRequest(apiUrl.toString(), map, loginUser.getId().intValue(), "BLlogout");
            if (null == apiResponseData || !"200".equals(apiResponseData.getJSONObject("resp_msg").getString("code"))) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            if ("200".equals(apiResponseData.getJSONObject("resp_msg").getString("code"))) {
                return Result.success();
            }
        } catch (Exception e) {
            logger.error("BLlog  BLlogout:{}", e);
            e.printStackTrace();
            return Result.failed();
        }
        return Result.failed();
    }


    /**
     * 公共请求
     */
    public JSONObject commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        logger.info("BLlog  {} commonRequest userId:{},paramsMap:{}", userId, params);
        JSONObject apiResponseData = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId);
        logger.info("BLlog  apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            apiResponseData = JSONObject.parseObject(resultString);
            logger.info("BLlog  {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, params, resultString, JSONObject.toJSONString(apiResponseData));
        }
        return apiResponseData;
    }

}
